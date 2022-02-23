package com.eshicha.email.config;

import java.util.ArrayList;
import java.util.List;

import com.eshicha.email.interceptor.SameUrlDataInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 *拦截器进行配置
 */
@SuppressWarnings("deprecation")
@SpringBootConfiguration
public class SpringMVCConfig extends WebMvcConfigurerAdapter {
	/**
	 * 防止重复提交拦截器
	 */
	@Autowired
	private SameUrlDataInterceptor sameUrlDataInterceptor;

	public void addInterceptors(InterceptorRegistry registry) {
		// 避开静态资源
		List<String> resourcePaths = defineResourcePaths();
		//全部进行拦截，也可以自己进行定义拦截，
		//addPathPatterns 拦截路劲
		//excludePathPatterns 忽略拦截路劲
		//registry.addInterceptor(sameUrlDataInterceptor).addPathPatterns("/**");不需要忽略使用
		registry.addInterceptor(sameUrlDataInterceptor).addPathPatterns("/**").excludePathPatterns(resourcePaths);// 重复请求
	}

	/**
	 * 自定义静态资源路径
	 * 
	 * @return
	 */

	public List<String> defineResourcePaths() {
		List<String> patterns = new ArrayList<>();
		patterns.add("/assets/**");
		patterns.add("/upload/**");
		patterns.add("/static/**");
		patterns.add("/common/**");
		patterns.add("/error");
		return patterns;
	}
}