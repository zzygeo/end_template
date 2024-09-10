package com.zzy.endtemplate.manager;

import com.zzy.endtemplate.config.RedisCache;
import com.zzy.endtemplate.constant.CacheConstants;
import com.zzy.endtemplate.constant.UserConstant;
import com.zzy.endtemplate.model.vo.LoginUser;
import com.zzy.endtemplate.utils.IpUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author zzy
 * @Description token相关工具类
 */

@Slf4j
@ConfigurationProperties(prefix = "token")
@Component
@Data
public class TokenManager {
    private String header;
    private String secret;
    private int expireTime;
    protected static final long MILLIS_SECOND = 1000;
    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;
    protected static final long MILLIS_HOUR = 60 * MILLIS_MINUTE;
    private static final Long MILLIS_MINUTE_TEN = 10 * 60 * 1000L;
    @Autowired
    RedisCache redisCache;

    // 从request里获取用户信息
    public LoginUser getLoginUser(HttpServletRequest request) {
        String token = getToken(request);
        if (!StringUtils.isEmpty(token)) {
            try {
                // 这里的解析是可能出现异常的
                Claims claims = parseToken(token);
                // 从map里获取uuid信息
                String uuid = (String) claims.get(UserConstant.USER_TOKEN_SALT);
                String userKey = getTokenKey(uuid);
                LoginUser loginUser = redisCache.getCacheObject(userKey);
                return loginUser;
            } catch (Exception e) {
                log.error("解析token失败");
            }
        }
        return null;
    }

    // 设置用户身份信息
    public void setLoginUser(LoginUser loginUser) {
        if (loginUser != null && !StringUtils.isEmpty(loginUser.getUuid())) {
            refreshToken(loginUser);
        }
    }

    // 删除用户身份信息
    public void delLoginUser(String uuid){
        if (!StringUtils.isEmpty(uuid)) {
            String userKey = getTokenKey(uuid);
            redisCache.deleteObject(userKey);
        }
    }

    // 创建令牌
    public String createToken(LoginUser loginUser){
        String uuid = UUID.randomUUID().toString();
        loginUser.setUuid(uuid);
        // 设置用户代理信息
        setUserAgent(loginUser);
        // 刷新token
        refreshToken(loginUser);
        HashMap<String, Object> claims = new HashMap<>();
        // 由于设置了randomUuid，所以每次生成的token都不一样
        claims.put(UserConstant.USER_TOKEN_SALT, uuid);
        return createToken(claims);
    }



    // 验证令牌有效期，相差不足20分钟，自动刷新缓存
    public void verifyToken(LoginUser loginUser) {
        Long expireTime = loginUser.getExpireTime();
        long currentTimeMillis = System.currentTimeMillis();
        if (expireTime - currentTimeMillis <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }

    // 刷新令牌有效期
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getUuid());
        // key userKey, value loginUser
        redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    // 设置用户代理信息
    public void setUserAgent(LoginUser loginUser) {
        String ip = IpUtils.getIpAddr();
        // 目前逻辑只设置ip
        loginUser.setIpaddr(ip);
    }

    // 从claims声明生成令牌
    private String createToken(Map<String, Object> claims) {
        String token = Jwts.builder()
                .setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }

    // 从令牌中获取数据声明
    private Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(secret)
                .parseClaimsJws(token).getBody();
    }

    // 从令牌中获取用户key UUID
    public String getUsernameFromToken(String token) {
        return parseToken(token).getSubject();
    }

    /**
     * 从request请求里拿到token信息
     * @param request
     * @return
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (!StringUtils.isEmpty(token) && token.startsWith(UserConstant.TOKEN_PREFIX)) {
            token = token.replace(UserConstant.TOKEN_PREFIX, "");
        }
        return token;
    }

    private String getTokenKey(String uuid) {
        return CacheConstants.LOGIN_TOKEN_KEY + uuid;
    }
}
