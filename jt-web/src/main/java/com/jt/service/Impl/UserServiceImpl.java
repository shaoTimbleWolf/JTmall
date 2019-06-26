package com.jt.service.Impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.jt.pojo.User;
import com.jt.service.UserService;
import com.jt.util.HttpClientService;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.SysResult;
@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private HttpClientService httpClientService;
	@Override
	public void insertUser(User user) {
//		String url = "http://sso.jt.com/user/register";
//		/*
//		 * //MD5 hash加密 String salt = UUID.randomUUID().toString(); SimpleHash
//		 * simpleHash = new SimpleHash("MD5", user.getPassword(), salt);
//		 * user.setPassword(simpleHash.toHex());
//		 */
//		//spring框架提供的DigestUtils工具api实现加密
//		String md5Digest = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
//		user.setPassword(md5Digest);
//		String json = ObjectMapperUtil.toJSON(user);
//		Map<String, String> params = new HashMap<>();
//		//将数据封装到map集合中，封装成json
//		params.put("userJSON",json);
//		String result = httpClientService.doPost(url, params);
//		
//		//判断返回值是否正确
//		SysResult sysResult = ObjectMapperUtil.toObject(result, SysResult.class);
//		if (sysResult.getStatus()==201) {//后台程序出错
//			throw new RuntimeException();
//		}
	}
}
