package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.OrderItemMapper;
import com.jt.mapper.OrderMapper;
import com.jt.mapper.OrderShippingMapper;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;
import com.jt.pojo.User;
import com.jt.util.ThreadLocalUtil;

//	编辑提供者jt-order的实现类
@Service(timeout=3000)
public class DubboOrderServiceImpl implements DubboOrderService{
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	
	@Transactional
	@Override
	public String insertOrder(Order order) {
		//0.准备orderId 用户ID+时间戳
		Date date = new Date();
		String orderId = "" + order.getUserId() + System.currentTimeMillis();
		//1.1设置Order的属性
		order.setOrderId(orderId).setStatus(1).setCreated(date).setUpdated(date);
		//1.2将order入库到order表
		orderMapper.insert(order);
		System.out.println("入库订单表");
		//2入库物流表
		//2.1从Order对象中获取OrderShipping对象
		OrderShipping orderShipping = order.getOrderShipping();
		orderShipping.setOrderId(orderId).setCreated(date).setUpdated(date);
		//2.2入库物流表
		orderShippingMapper.insert(orderShipping);
		System.out.println("入库物流表");
		//3订单商品入库
		//3.1从Order中获取OrderItem对象的集合
		List<OrderItem> orderItems = order.getOrderItems();
		//3.2循环遍历入库
		for (OrderItem orderItem : orderItems) {
			orderItem.setOrderId(orderId).setCreated(date).setUpdated(date);
			orderItemMapper.insert(orderItem);
		}
		System.out.println("入库商品订单表");
		return orderId;
	}

	@Override
	public Order findOrderById(String id) {
		//1.查询三张表信息,此时的id是orderId
		Order order = orderMapper.selectById(id);
		OrderShipping orderShipping = orderShippingMapper.selectById(id);
		QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("item_id", id);
		List<OrderItem> orderItemList = orderItemMapper.selectList(queryWrapper);
		//2.将OrderShipping对象和OrderItem对象的集合存入order对象
		order.setOrderItems(orderItemList).setOrderShipping(orderShipping);
		return order;
	}
}








