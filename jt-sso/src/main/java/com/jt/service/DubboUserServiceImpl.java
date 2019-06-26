package com.jt.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;

//该类是dubbo的实现类
@Service(timeout=3000)
public class DubboUserServiceImpl implements DubboUserService{
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private JedisCluster jedisCluster;

	@Override
	public void insertUser(User user) {
		//1.将密码加密
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		//2.设置参数
		user.setPassword(md5Pass).setEmail(user.getPhone()).setCreated(new Date()).setUpdated(new Date());
		userMapper.insert(user);
	}
	/**
	 * 1.校验用户名和密码是否正确
	 * 2.如果数据不正确 返回null值
	 * 3.如果数据正确
	 * 		1)生成加密秘钥:MD5(JT_TICKET_+username+当前毫秒数)
	 * 		2)将userDB的数据转换为json
	 * 		3)将数据保存到redis中，7天超时
	 * 		4)返回加密的秘钥
	 */
	@Override
	public String findUserByUP(User user) {
		String token = null;
		//1.校验用户名和密码是否正确
		//1.1将密码进行加密
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>(user);
		User userDB = userMapper.selectOne(queryWrapper);
		//2.判断是否正确,正确执行业务
		if (userDB!=null) {
			token = "JT_TICKET_"+userDB.getUsername()+System.currentTimeMillis();
			token = DigestUtils.md5DigestAsHex(token.getBytes());
			//脱敏处理
			userDB.setPassword("你猜猜!!!!");
			String userJSON = ObjectMapperUtil.toJSON(userDB);
			//将用户信息存入redis
			jedisCluster.setex(token, 7*24*60*60, userJSON);
		}
			
		return token;
		
	}
}
