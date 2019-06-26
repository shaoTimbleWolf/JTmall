package com.jt.interceptor;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.StringUtils;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.ThreadLocalUtil;

import redis.clients.jedis.JedisCluster;
@Component//将拦截器交给spring容器管理
public class UserInterceptor implements HandlerInterceptor{
	
	@Autowired
	private JedisCluster jedisCluster;
	/**
	 * 	在spring4版本中不管是否需要都要重新三个方法
	 * 	在spring5版本中，添加了default属性，则省略不写，需要的时候再重写
	 * 	preHandle业务方法执行之前拦截，postHandle业务调用的时候执行，afterCompletion视图渲染后执行
	 * 
	 */

	/**
	 * 	用户未登陆之前拦截，限制其操作
	 * 	返回值：true 拦截放行  false 请求拦截   重定向到登陆页面
	 * 
	 * 	业务逻辑：1.获取cookies的数据
	 * 			2.从cookie中获取token(JT_TICKET)
	 * 			3.判断redis缓存服务器中是否有数据
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String token = null;
		//1.获取cookie信息
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if ("JT_TICKET".equals(cookie.getName())) {
				token = cookie.getValue();
				break;
			}
		}
		//2.判断token是否有效
		if (!StringUtils.isEmpty(token)) {//token有数据，返回true
			//4.判断redis中是否有数据
			String userJSON = jedisCluster.get(token);
			if (!StringUtils.isEmpty(userJSON)) {//redis中有用户数据
				User user = ObjectMapperUtil.toObject(userJSON, User.class);
				ThreadLocalUtil.set("JT_USER", user);
				return true;
			}
		}
		//3.token无数据，直接拦截，返回false重定向到登录页面
		response.sendRedirect("/user/login.html");
		return false;
	}
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
