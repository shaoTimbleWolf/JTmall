package com.jt.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//redis配置类
import org.springframework.context.annotation.PropertySource;

import lombok.val;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
@Configuration
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {
	@Value("${redis.nodes}")
	private String redisNodes;
	@Bean
	public JedisCluster jedisCluster() {
		Set<HostAndPort> nodes = new HashSet<>();
		//1.按照 , 拆分
		String[] node = redisNodes.split(",");
		//2.按照 ：拆分 获取host和port
		/**for循环由于要计算集合的长度，效率上要低于foreach循环*/
		for (String nodeArray : node) {
			String host = nodeArray.split(":")[0];
			/**valueOf返回的是Integer类型的对象，自动拆装后也是int类型，底层封装了parseInt方法
			 * parseInt是将对象转换为int类型，效率上比valueof高 */
			//int port =Integer.valueOf(nodeArray.split(":")[1]);
			int port =Integer.parseInt(nodeArray.split(":")[1]);
			nodes.add(new HostAndPort(host, port));
		}
		return new JedisCluster(nodes);
	}
	
//	@Value("${redis.sentinels}")
//	private String jedisSentinelNodes; 
//	@Value("${redis.sentinel.masterName}")
//	private String masterName;
//	/**
//	 * redis哨兵机制实现操作
//	 * @return
//	 */
//	@Bean
//	public JedisSentinelPool jedisSentinelPool() {
//		//masterName 代表主机的变量名称
//		HashSet<String> sentinels = new HashSet<String>();
//		sentinels.add(jedisSentinelNodes);
//		return new JedisSentinelPool(masterName, sentinels);
//	}
//	
	
//  redis集群	分片
//	@Value("${redis.nodes}")
//	private String redisNodes;
//	@Bean
//	public ShardedJedis shardedJedis() {
//		List<JedisShardInfo> shards = new ArrayList<>();
//		String[] nodes=redisNodes.split(",");//ip:端口
//		for (String node : nodes) {
//			String host = node.split(":")[0];
//			int port = Integer.parseInt(node.split(":")[1]);
//			JedisShardInfo info = new JedisShardInfo(host,port);
//			shards.add(info);
//		}
//		return new ShardedJedis(shards);
//	}
}
