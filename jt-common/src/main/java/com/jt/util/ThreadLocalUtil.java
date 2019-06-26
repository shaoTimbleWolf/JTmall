package com.jt.util;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalUtil {
	private static ThreadLocal<Map<String, Object>> thread = new ThreadLocal<>();
	//存放数据
	//存放多个数据时候用map
	public static void set(String key,Object value) {
		HashMap<String,Object> map = new HashMap<>();
		map.put(key, value);
		thread.set(map);
	
	}
	//获取数据
	public static Object get(String key) {
		return thread.get().get(key);
		 
	}
	//使用threadLocal本地线程变量时,需要额外的注意内存泄漏问题.
	public static void remove() {
		thread.remove();//移除threadlocal
	}
}
