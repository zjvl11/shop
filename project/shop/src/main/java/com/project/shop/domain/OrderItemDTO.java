package com.project.shop.domain;

import com.project.shop.entity.OrderItem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDTO {

	private String itemName;
	
	private int count;
	
	private int orderPrice;
	
	private String imgUrl;
	
	public OrderItemDTO(OrderItem orderItem, String imgUrl) {
		this.itemName = orderItem.getItem().getItemName();
		this.count = orderItem.getCount();
		this.orderPrice = orderItem.getOrderPrice();
		this.imgUrl = imgUrl;
	}
}
