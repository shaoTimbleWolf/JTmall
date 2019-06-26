package com.jt.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jt.anno.Cache_Find;
import com.jt.enu.KEY_ENUM;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.RedisService;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.ShardedJedis;

@Component//交给spring容器管理
@Aspect//标识切面
public class RedisAspect {
	//哨兵集群
	@Autowired(required=false)
	private JedisCluster jedis;
	
	//引入哨兵机制 1000个连接
//	@Autowired(required=false)
//	private RedisService jedis;
	
	//required=false
	//	容器在初始化时不需要实例化该对象
	//	只有在使用时才初始化，一般工具类中添加该注解
//	@Autowired
//	private Jedis jedis;
//	@Autowired
//	private ShardedJedis jedis;
	//	@annotation(cache_find)使用该方法可以获得注解的对象
	/**
	 * 
	 * @param jointPoint
	 * @param cache_find
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Around("@annotation(cache_find)")
	public Object around(ProceedingJoinPoint jointPoint, Cache_Find cache_find) {
		//	获取key的值
		String key=getKey(jointPoint,cache_find);
		//	根据key查询缓存
		String result = jedis.get(key);
		Object data = null;
		try {
			if (StringUtils.isEmpty(result)) {
				//result为null缓存中没有数据，执行业务方法查询数据库
				data = jointPoint.proceed();//执行业务方法
				String json = ObjectMapperUtil.toJSON(data);
				if (cache_find.seconds() == 0) {
					//表示不超时 
					jedis.set(key, json);
					System.out.println(11);
				}else {
					System.out.println("qweqe");
					jedis.setex(key, cache_find.seconds(), json);
					System.out.println("查询数据库");
				}
			}else {//redis缓存中有数据，从缓存中取数据
				Class tarClass = getTargetClass(jointPoint);
				data = ObjectMapperUtil.toObject(result, tarClass);
				System.out.println("查询redis缓存");
			}
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return data;
	}
	private Class getTargetClass(ProceedingJoinPoint jointPoint) {
		MethodSignature signature =(MethodSignature)jointPoint.getSignature();
		return signature.getReturnType();
	}
	/**
	 * 1.判断用户key的类型auto empty
	 * 
	 * @param jointPoint
	 * @param cache_find
	 * @return
	 */
	private String getKey(ProceedingJoinPoint jointPoint, Cache_Find cache_find) {
		//1.获取key的类型
		KEY_ENUM keyType = cache_find.keyType();
		//2.判断key的类型
		if (keyType.equals(KEY_ENUM.EMPTY)) {//表示用户使用自己的key
			return cache_find.key();
		}else {
			//	表示用户自己的key需要拼接  key+_+第一个参数
			String strArgs = String.valueOf(jointPoint.getArgs()[0]);
			String key = cache_find.key()+"_"+strArgs;
			return key;
		}
	}
}
