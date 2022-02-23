package com.eshicha.email.interceptor;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eshicha.email.config.SameUrlData;
import com.eshicha.email.entities.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;

/**
 * @Title: 防止用户重复提交数据拦截器
 * @Description: 将用户访问的url和参数结合token存入redis，每次访问进行验证是否重复请求接口
 */
@Component
public class SameUrlDataInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	StringRedisTemplate smsRedisTemplate;
    private static Logger LOG = LoggerFactory.getLogger(SameUrlDataInterceptor.class);

    /**
     * 是否阻止提交,fasle阻止,true放行
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            SameUrlData annotation = method.getAnnotation(SameUrlData.class);
            if (annotation != null) {
                if(repeatDataValidator(request)){
                    //请求数据相同
                    LOG.warn("please don't repeat submit,url:"+ request.getServletPath());
                  //前后端分离使用respone返回参数
                    JSONObject result = new JSONObject();
                    result.put("code","500");
                    result.put("msg","服务器资源有限，请勿重复提交！");
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json; charset=utf-8");
                    response.getWriter().write(result.toString());
                    response.getWriter().close();
                    //如果不是前后端分离可以进行页面转发
//                    拦截之后跳转页面
//                    String formRequest = request.getRequestURI();
//                    request.setAttribute("myurl", formRequest);
//                    request.getRequestDispatcher("/WebRoot/common/error/jsp/error_message.jsp").forward(request, response);
                    return false;
                }else {//如果不是重复相同数据
                    return true;
                }
            }
            return true;
        } else {
            return super.preHandle(request, response, handler);
        }
    }
    /**
     * 验证同一个url数据是否相同提交,相同返回true
     * @param httpServletRequest
     * @return
     */
    public boolean repeatDataValidator(HttpServletRequest httpServletRequest) throws Exception {
        //获取请求参数map
        Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        Iterator<Map.Entry<String, String[]>> it = parameterMap.entrySet().iterator();
        Map<String, String[]> parameterMapNew = new HashMap<>();
        while(it.hasNext()){
            Map.Entry<String, String[]> entry = it.next();
                parameterMapNew.put(entry.getKey(), entry.getValue());
        }
        //过滤过后的请求内容
        String params = JSONObject.toJSONString(parameterMapNew);
        String url = httpServletRequest.getRequestURI();
        Map<String,String> map = new HashMap<>();
        //key为接口，value为参数
        map.put(url, params);
        String nowUrlParams = map.toString();
        String redisKey = getIp(httpServletRequest) + url;
        String preUrlParams = smsRedisTemplate.opsForValue().get(redisKey);
        if(preUrlParams == null){
            //如果上一个数据为null,表示还没有访问页面
            //存放并且设置有效期，2秒
            smsRedisTemplate.opsForValue().set(redisKey, nowUrlParams, 15, TimeUnit.SECONDS);
            return false;
        }else{//否则，已经访问过页面
            if(preUrlParams.equals(nowUrlParams)){
                //如果上次url+数据和本次url+数据相同，则表示重复添加数据
                return true;
            }else{//如果上次 url+数据 和本次url加数据不同，则不是重复提交
                smsRedisTemplate.opsForValue().set(redisKey, nowUrlParams, 15, TimeUnit.SECONDS);
                return false;
            }
        }
    }

    public String getIp(HttpServletRequest request) throws Exception {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null) {
            if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
                int index = ip.indexOf(",");
                if (index != -1) {
                    return ip.substring(0, index);
                } else {
                    return ip;
                }
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (ip != null) {
            if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        ip = request.getHeader("Proxy-Client-IP");
        if (ip != null) {
            if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip != null) {
            if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        ip =  request.getRemoteAddr();
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }
}