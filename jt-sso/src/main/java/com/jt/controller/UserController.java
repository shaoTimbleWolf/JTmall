package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.service.UserService;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@RestController
//http://sso.jt.com/user/check/
@RequestMapping("/user/")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private JedisCluster jedisCluster;
	
	
	/**
	 * 业务说明：校验用户是否存在
	 * url:http://sso.jt.com/user/check/{param}/{type}
	 * @param param
	 * @param type
	 * @return 返回值为SysResult对象，由于跨域请求，返回值必须特殊处理callback(json)
	 */
	@RequestMapping("check/{param}/{type}")
	public JSONPObject check(@PathVariable String param,
							@PathVariable Integer type,
							String callback) {
		JSONPObject object = null;
		try {
			//判断用户信息是否存在，存在返回false，不存在返回true，并且将信息传递到页面
			Boolean flag = userService.checkUser(param,type);
			object = new JSONPObject(callback,SysResult.ok(flag));
		} catch (Exception e) {
			//如果程序出错，直接返回失败的状态信息
			e.printStackTrace();
		
			object = new JSONPObject(callback, SysResult.fail());
		}
		return object;
	}
	@RequestMapping("register")
	public SysResult insertUser(String userJSON) {
		
		try {
			User user = ObjectMapperUtil.toObject(userJSON, User.class);
			userService.insertUser(user);
			return new SysResult().ok();
		} catch (Exception e) {
			e.printStackTrace();
			return new SysResult().fail();
		}
	}
	//利用跨域实现用户信息的回显
	//http://sso.jt.com/user/query/06cb808e533b74a202bcb5ab61f9eb35?callback=jsonp1560762200636&_=15
	@RequestMapping("query/{ticket}")
	public JSONPObject findUserByTicket(@PathVariable String ticket,String callback) {
		String userJSON = jedisCluster.get(ticket);
		if (StringUtils.isEmpty(userJSON)) {
			//回显的数据要经过200判断，返回值为SysResult
			return new JSONPObject(callback,SysResult.fail());
		}else {
			return new JSONPObject(callback,SysResult.ok(userJSON));
		}
	}
	
	
}
