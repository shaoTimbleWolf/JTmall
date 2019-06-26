package com.jt.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jt.enu.KEY_ENUM;

/**
 *	自定义了一个查询的缓存注解
 */
@Retention(RetentionPolicy.RUNTIME)//程序运行时有效
@Target(ElementType.METHOD)//注解作用的范围，在方法上有效
//{ElementType.METHOD,ElementType.FIELD}表示该注解在方法上和属性上有效
public @interface Cache_Find {
	String key() default "";	//用户传值的key
	KEY_ENUM keyType() default KEY_ENUM.AUTO;	//默认自动拼接
	int seconds() default 0;	//数据保存的时间，为0默认永久保存
}
