package com.jt.service;

import com.jt.pojo.User;

public interface UserService {
	/**
	 * 检查用户信息
	 * @param param 参数  username/phone
	 * @param type 类型 1-username  2-phone
	 * @return
	 */
	Boolean checkUser(String param, Integer type);

	void insertUser(User user);

}
