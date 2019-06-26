package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;
import com.jt.pojo.User;

//	编辑提供者jt-cart的实现类
@Service(timeout=3000)
public class DubboCartServiceImpl implements DubboCartService{
	@Autowired
	private CartMapper cartMapper;

	@Override
	public List<Cart> findCartListByUserId(Long userId) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", userId);
		List<Cart> cartList = cartMapper.selectList(queryWrapper);
		return cartList;
	}
	/**
	 * 	更新的数据信息：更新num和updatedtime
	 * 	判断条件user_id and item_id
	 */
	@Transactional
	@Override
	public void updateCarNum(Cart cart) {
		Cart tempCart = new Cart();
		tempCart.setNum(cart.getNum()).setUpdated(new Date());
		UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("user_id", cart.getUserId()).eq("item_id", cart.getItemId());
		cartMapper.update(tempCart, updateWrapper);
	}
	@Transactional
	@Override
	public void deleteCart(Cart cart) {
		//mybatis中的条件构造器中如果有两个属性不为空，就可以当做where条件
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>(cart);

		cartMapper.delete(queryWrapper);

	}
	@Transactional
	@Override
	public void insertCart(Cart cart) {
		//	用户第一次入库，直接入库
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", cart.getUserId()).eq("item_id", cart.getItemId());
		Cart cartDB = cartMapper.selectOne(queryWrapper);
		if (cartDB == null) {//	用户第一次加入购物车，直接入库
			cart.setCreated(new Date()).setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		}else {//	用户已经添加了该商品，只做数量的修改
			int num = cart.getNum() + cartDB.getNum();
			cartDB.setNum(num).setUpdated(new Date());
			/**
			 *	 修改操作的时候，所有不为空的数据都会参与sql
			 *	update tb_cart
			 *		set num=#{num},updated=#{update},
			 *			user_id=#{userId},item_id=#{itemId}......(全部的列字段)
			 *		where id = #{id}
			 *	此时数据库的效率比较低，尤其数据量特别大的时候
			 *	在此情况下，自己写的SQL语句执行效率要高于下面的mybatis-plus的效率
			 */
			cartMapper.updateById(cartDB);
		}
	}

}
