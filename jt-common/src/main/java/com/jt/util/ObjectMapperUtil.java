package com.jt.util;

import javax.management.RuntimeErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;

//编辑工具类实现对象和json的转化
public class ObjectMapperUtil {
	private static final ObjectMapper MAPPER = new ObjectMapper();
	//将对象转换为json
	public static String toJSON(Object target) {
		String json = null;
		try {
			json = MAPPER.writeValueAsString(target);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return json;
	}
	//将json转换为对象
	public static <T> T toObject(String json,Class<T> cls) {
		T target = null;
		try {
			target = MAPPER.readValue(json, cls);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return target;
	}
}
