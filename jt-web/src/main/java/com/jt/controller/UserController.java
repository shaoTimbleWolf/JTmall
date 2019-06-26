package com.jt.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.javassist.expr.NewArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.service.UserService;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user/")
public class UserController {
	//	@Autowired
	//	private UserService userService;
	//导入dubbo用户的接口
	@Reference(timeout=3000,check=false)
	private DubboUserService userService;
	@Autowired
	private JedisCluster jedisCluster;

	@RequestMapping("{modelName}")
	public String index(@PathVariable String modelName) {

		return modelName;
	}
	@RequestMapping("doRegister")
	@ResponseBody
	public SysResult doRegister(User user) {
		try {
			
			userService.insertUser(user);
			return new SysResult().ok();
		} catch (Exception e) {
			e.printStackTrace();
			return new SysResult().fail();
		}
	}
	/**
	 * 实现登录
	 * 利用response对象将cookie写入客户端
	 * cookie的生命周期 
	 * 		cookie.setMaxAge(数值) 数值为0时,立即删除;大于0值均为可存活的秒数;-1时,会话结束后删除
	 * cookie.setPath("/");cookie的权限路径，一般默认都是"/"
	 * cookie.setDomain("jt.com");实现数据的共享
	 * @param user
	 * @return
	 */
	@RequestMapping("doLogin")
	@ResponseBody
	public SysResult doLogin(User user,HttpServletResponse response) {
		try {
			//调用sso系统获取秘钥
			String token = userService.findUserByUP(user);
			//判断数据是否正确,为空判断,不为空将数据保存到cookie中
			//cookie中的key是固定值：JT_TICKET
			if (!StringUtils.isEmpty(token)) {
				Cookie cookie = new Cookie("JT_TICKET", token);
				cookie.setMaxAge(7*24*3600);//生命周期
				cookie.setDomain("jt.com");//要求所有的xx.jt.com共享cookie，实现数据的共享
				cookie.setPath("/");//cookie的权限路径
				response.addCookie(cookie);
				return SysResult.ok();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  SysResult.fail();
	}

	/**
	 * 	实现退出操作
	 * 	url: http://www.jt.com/user/logout.html
	 * 	删除redis
	 * 	删除cookie
	 * @return 重定向到首页
	 */
	@RequestMapping("logout")
	public String logout(HttpServletRequest request,HttpServletResponse response) {
		//1.获取所有的cookie的name
		Cookie[] cookies = request.getCookies();
		String token = null;
		for (Cookie ck : cookies) {
			if (cookies.length != 0) {
				if ("JT_TICKET".equals(ck.getName())) {
					//2.获取redis的token
					token = ck.getValue();
				
				}
			}

			if (!StringUtils.isEmpty(token)) {
				jedisCluster.del(token);
				//3.删除cookie
				Cookie cookie = new Cookie("JT_TICKET", "");
				cookie.setMaxAge(0);
				cookie.setPath("/");
				cookie.setDomain("jt.com");
				response.addCookie(cookie);
		}

		}
		return "redirect:/";
	}
}
