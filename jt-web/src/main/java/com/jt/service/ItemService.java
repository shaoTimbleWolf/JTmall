package com.jt.service;

import org.springframework.stereotype.Service;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;

public interface ItemService {

	Item findItemById(Long itemId);

	ItemDesc findItemDescById(Long itemId);


}
