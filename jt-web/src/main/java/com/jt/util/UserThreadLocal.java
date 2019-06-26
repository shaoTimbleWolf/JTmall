package com.jt.util;


import com.jt.pojo.User;

public class UserThreadLocal {
	private static ThreadLocal<User> thread = new ThreadLocal<>();
	//存放数据
	//存放多个数据时候用map
	public static void set(User user) {
		thread.set(user);
	
	}
	//获取数据
	public static Object get() {
		return thread.get();
		 
	}
	//使用threadLocal本地线程变量时,需要额外的注意内存泄漏问题.
	public static void remove() {
		thread.remove();//移除threadlocal
	}

}
