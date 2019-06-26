package com.jt.service;

import java.util.List;

import com.jt.vo.EasyUITree;

public interface ItemCatService {

	String findItemCatNameById(Long itemCatId);
	
	List<EasyUITree> findItemCatByParentId(Long parentId);
	//从缓存查询商品的分类信息
//	List<EasyUITree> findItemCatByCache(Long parentId);

}
