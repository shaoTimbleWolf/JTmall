package com.jt.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.SysResult;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	/**
	 * 检查用户信息是否存在
	 * @param param 参数  username/phone/email
	 * @param type 类型 1-username  2-phone 3-email
	 * 将type转换为具体的字段
	 * @return ture表示已经存在  false表示用户名可以使用
	 */
	@Override
	public Boolean checkUser(String param, Integer type) {
		//将type转换为具体的字段
		String column =(type==1)?"username":((type==2)?"phone":"email");
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(column, param);
		Integer count = userMapper.selectCount(queryWrapper);
		//查询有结果则返回false，无结果返回true
		return (count==0)?false:true;
	}
	@Transactional//事务控制
	@Override
	public void insertUser(User user) {
		user.setEmail(user.getPhone()).setUpdated(new Date()).setUpdated(new Date());
		userMapper.insert(user);

	}



}
