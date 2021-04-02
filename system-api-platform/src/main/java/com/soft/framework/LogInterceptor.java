package com.soft.framework;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.soft.Entity.LoggerEntity;
import com.soft.util.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 记录访问日志
 * @ClassName LogInterceptor
 * @Description: TODO
 * @Author caidao
 * @Date 2020/9/28
 * @Version V1.0
 **/
public class LogInterceptor implements HandlerInterceptor {

    //请求开始时间标识
    private static final String LOGGER_SEND_TIME = "_send_time";

    private static final String LOGGER_ENTITY = "_logger_entity";


    private final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求参数信息
        LoggerEntity loggerEntity = new LoggerEntity();
        String param = JSONObject.toJSONString(request.getParameterMap(),
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue);
        //设置请求开始时间
        request.setAttribute(LOGGER_SEND_TIME,System.currentTimeMillis());
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            loggerEntity.setClassName(handlerMethod.getBean().getClass().getName());
            loggerEntity.setClassMethod(handlerMethod.getMethod().getClass().getName());
        }
        loggerEntity.setParamData(param);
        loggerEntity.setMethod(request.getMethod());// 请求方法,GET,POST...
//        loggerEntity.setType();
        loggerEntity.setUri(request.getRequestURI());// 请求路径
        loggerEntity.setSessionId(request.getRequestedSessionId());
        request.setAttribute(LOGGER_ENTITY,loggerEntity);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        logger.info("********××××××*日志输出end[aft]××××××××××××××××");
        //请求开始时间
        long time = Long.valueOf(request.getAttribute(LOGGER_SEND_TIME).toString());
        long currentTime = System.currentTimeMillis(); //设置当前时间
//        logger.info("此次请求耗时："+Integer.valueOf((currentTime - time)+"")/1000 +" s");// 耗时
        LoggerEntity loggerEntity = (LoggerEntity)request.getAttribute(LOGGER_ENTITY);
        loggerEntity.setTimeConsuming(Long.valueOf(currentTime - time) + "");
        loggerEntity.setHttpStatus(response.getStatus());//获取请求错误码
        loggerEntity.setReturnData(currentTime+"");
        loggerEntity.setReturnData(request.getAttribute(LoggerUtil.LOGGER_RETURN).toString());
        logger.info("*****************************************");
        logger.info("完整日志输出->"+JSONObject.toJSONString(loggerEntity) );
        logger.info("*****************************************");
    }
}
