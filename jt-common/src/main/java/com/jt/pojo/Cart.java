package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_cart")
public class Cart extends BasePojo{
	@TableId(type=IdType.AUTO)
	private Long id;
	private Long userId;
	private Long itemId;
	private String itemTitle;
	private String itemImage;//保存的是商品的第一张图片信息
	private Integer itemPrice;
	private Integer num;
	
	
	
}
