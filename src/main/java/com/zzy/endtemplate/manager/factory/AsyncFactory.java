package com.zzy.endtemplate.manager.factory;

import com.zzy.endtemplate.common.Constants;
import com.zzy.endtemplate.model.entity.LoginInfo;
import com.zzy.endtemplate.model.entity.OperLog;
import com.zzy.endtemplate.service.LoginInfoService;
import com.zzy.endtemplate.service.OperLogService;
import com.zzy.endtemplate.utils.IpUtils;
import com.zzy.endtemplate.utils.LogUtils;
import com.zzy.endtemplate.utils.ServletUtils;
import com.zzy.endtemplate.utils.SpringUtils;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.TimerTask;


/**
 * @Author zzy
 * @Description 异步工厂
 */
public class AsyncFactory {
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 记录登陆信息
     * @param userName 用户账户名
     * @param status 登陆状态
     * @param message 登陆消息
     * @param args 参数
     * @return
     */
    public static TimerTask recordLogininfor(final String userName, final String status, final String message, final Object... args) {
        final String ip = IpUtils.getIpAddr();
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        return new TimerTask() {
            @Override
            public void run() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(LogUtils.getBlock(ip));
                stringBuilder.append(LogUtils.getBlock(userName));
                stringBuilder.append(LogUtils.getBlock(status));
                stringBuilder.append(LogUtils.getBlock(message));
                sys_user_logger.info(stringBuilder.toString(), args);
                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                LoginInfo loginInfo = new LoginInfo();
                loginInfo.setUserName(userName);
                loginInfo.setIpAddr(ip);
                loginInfo.setBrowser(browser);
                loginInfo.setOs(os);
                loginInfo.setMsg(message);
                loginInfo.setLoginTime(new Date());
                // 日志状态
                if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER))
                {
                    loginInfo.setStatus(Constants.SUCCESS);
                }
                else if (Constants.LOGIN_FAIL.equals(status))
                {
                    loginInfo.setStatus(Constants.FAIL);
                }
                // 插入数据
                SpringUtils.getBean(LoginInfoService.class).save(loginInfo);
            }
        };
    }

    // 操作日志表
    public static TimerTask recordOper(final OperLog operLog) {
        return new TimerTask() {
            @Override
            public void run() {
                SpringUtils.getBean(OperLogService.class).addOperLog(operLog);
            }
        };
    }
}
