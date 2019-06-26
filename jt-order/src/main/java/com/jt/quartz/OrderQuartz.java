package com.jt.quartz;

import java.util.Calendar;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.OrderMapper;
import com.jt.pojo.Order;

//准备订单定时任务
@Component
public class OrderQuartz extends QuartzJobBean{

	@Autowired
	private OrderMapper orderMapper;

	/**	当用户订单提交30分钟后,如果还没有支付.则交易关闭
	 * 	现在时间 - 订单创建时间 > 30分钟  则超时
	 * 	new date - 30 分钟 > 订单创建时间
	 */
	@Override
	@Transactional
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		//	设定30分钟超时
		/**
		 * 	业务思想：
		 * 		用户30分钟未支付则将其状态修改为6-交易关闭
		 * SQL：update tb_order set status=6 
		 * 		where status = 1 and created < now date() - 30minutes;
		 */
		Calendar calendarcal = Calendar.getInstance();//获取当前时间
		calendarcal.add(calendarcal.MINUTE, -30);
		Date timeOutDate = calendarcal.getTime();
		Order order = new Order();
		order.setStatus(6).setUpdated(new Date());
		UpdateWrapper<Order> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("status", 1).lt("created", timeOutDate);
		orderMapper.update(order, updateWrapper);
		
	}
}
