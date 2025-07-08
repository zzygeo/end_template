package com.zzy.biaohui.aop;
/**
 * @Author zzy
 * @Description 操作日志注解
 */

import com.alibaba.fastjson2.JSON;
import com.zzy.biaohui.annotation.Log;
import com.zzy.biaohui.exception.BusinessException;
import com.zzy.biaohui.filter.PropertyPreExcludeFilter;
import com.zzy.biaohui.manager.AsyncManager;
import com.zzy.biaohui.manager.factory.AsyncFactory;
import com.zzy.biaohui.model.entity.OperLog;
import com.zzy.biaohui.model.enums.BusinessStatus;
import com.zzy.biaohui.model.vo.LoginUser;
import com.zzy.biaohui.utils.IpUtils;
import com.zzy.biaohui.utils.ServletUtils;
import com.zzy.biaohui.utils.UserContext;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;

@Component
@Aspect
public class LogAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);
    public static final String[] EXCLUDE_WORDS = {"password"};
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<Long>("Cost Time");

    /**
     * 处理请求前执行
     * @param joinPoint
     * @param controllerLog
     */
    @Before(value = "@annotation(controllerLog)")
    public void doBefore(JoinPoint joinPoint, Log controllerLog) {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    /**
     * 处理完请求后执行
     * @param joinPoint
     * @param controllerLog
     * @param jsonResult
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     * @param joinPoint
     * @param controllerLog
     * @param e
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        if (e instanceof BusinessException) {
            BusinessException customException = (BusinessException) e;
            log.error("异常信息:{}", customException.getMessage());
        }
        handleLog(joinPoint, controllerLog, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult)  {
        try {
            // 获取当前的用户
            LoginUser loginUser = UserContext.getLoginUser();

            // *========数据库日志=========*//
            OperLog operLog = new OperLog();
            operLog.setStatus(BusinessStatus.SUCCESS.getCode());
            String ip = IpUtils.getIpAddr();
            operLog.setOperIp(ip);
            String requestURI = ServletUtils.getRequest().getRequestURI();
            operLog.setOperUrl(requestURI);
            if (loginUser != null) {
                operLog.setOperName(loginUser.getUser().getUserName());
            }
            // 如果出现错误
            if (e != null) {
                operLog.setStatus(BusinessStatus.FAIL.getCode());
                if (StringUtils.isNotEmpty(e.getMessage())) {
                    String msg = e.getMessage().length() > 2000 ? e.getMessage().substring(0, 2000) : e.getMessage();
                    operLog.setErrorMsg(msg);
                }
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setOperMethod(className + "." + methodName + "()");
            getControllerMethodDescription(joinPoint, controllerLog, operLog, jsonResult);
            // 设置消耗时间
            operLog.setCostTime(System.currentTimeMillis() - TIME_THREADLOCAL.get());
            // 保存到数据库
            AsyncManager.me().execute(AsyncFactory.recordOper(operLog));
        } catch (Exception exception) {
            log.error("异常信息：{}", exception.getMessage());
            exception.printStackTrace();
        } finally {
            TIME_THREADLOCAL.remove();
        }
    }

    /**
     * 设置执行结果
     * @param joinPoint
     * @param log
     * @param operLog
     * @param jsonResult
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, OperLog operLog, Object jsonResult) {
        // 设置action动作
        operLog.setBusinessType(log.businessType().getCode());
        operLog.setTitle(log.title());
        if (log.isSaveRequestData()) {
            setRequestValue(joinPoint, operLog, log.excludeParamNames());
        }
        if (log.isSaveResponseData() && (jsonResult != null && !"".equals(jsonResult))) {
            String json = JSON.toJSONString(jsonResult).length() > 2000 ? JSON.toJSONString(jsonResult).substring(0, 2000) : JSON.toJSONString(jsonResult);
            operLog.setJsonResult(JSON.toJSONString(json));
        }
    }

    /**
     * 设置请求参数
     * @param joinPoint
     * @param operLog
     * @param excludeParams
     */
    private void setRequestValue(JoinPoint joinPoint, OperLog operLog, String[] excludeParams) {
        Map<?, ?> paramMap = ServletUtils.getParamMap(ServletUtils.getRequest());
        String method = ServletUtils.getRequest().getMethod();
        if ((paramMap == null || paramMap.isEmpty()) && (HttpMethod.PUT.name().equals(method) || HttpMethod.POST.name().equals(method))) {
            String params = argsArrayToString(joinPoint.getArgs(), excludeParams);
            params = params.length() > 2000 ? params.substring(0, 2000) : params;
            operLog.setOperParam(params);
        } else {
            // 如果是
            String jsonString = JSON.toJSONString(paramMap, excludePropertyPreFilter(excludeParams));
            jsonString = jsonString.length() > 2000 ? jsonString.substring(0, 2000) : jsonString;
            operLog.setOperParam(jsonString);
        }
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray, String[] excludeParamNames)
    {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0)
        {
            for (Object o : paramsArray)
            {
                if (o != null && !isFilterObject(o))
                {
                    try
                    {
                        String jsonObj = JSON.toJSONString(o, excludePropertyPreFilter(excludeParamNames));
                        params += jsonObj.toString() + " ";
                    }
                    catch (Exception e)
                    {
                    }
                }
            }
        }
        return params.trim(); // 去除末尾的空格
    }

    /**
     * 忽略敏感属性
     */
    public PropertyPreExcludeFilter excludePropertyPreFilter(String[] excludeParamNames)
    {
        return new PropertyPreExcludeFilter().addExcludes(ArrayUtils.addAll(EXCLUDE_WORDS, excludeParamNames));
    }

    /**
     * 判断是否需要过滤的对象。
     * @param o
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    public boolean isFilterObject(final Object o)
    {
        Class<?> clazz = o.getClass();
        if (clazz.isArray())
        {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        }
        else if (Collection.class.isAssignableFrom(clazz))
        {
            Collection collection = (Collection) o;
            for (Object value : collection)
            {
                return value instanceof MultipartFile;
            }
        }
        else if (Map.class.isAssignableFrom(clazz))
        {
            Map map = (Map) o;
            for (Object value : map.entrySet())
            {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}
