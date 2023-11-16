package com.project.shop.domain;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.project.shop.constant.OrderStatus;
import com.project.shop.entity.Order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderHistDTO {

	
	private Long orderId;
	
	private String orderDate;
	
	private OrderStatus orderStatus;
	
	//주문 상품 리스트
	private List<OrderItemDTO> orderItemDtoList = new ArrayList<>();
	
	public void addOrderItemDto(OrderItemDTO orderItemDto) {
		orderItemDtoList.add(orderItemDto);
	}
	
	
	public OrderHistDTO(Order order) {
		this.orderId = order.getId();
		this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		this.orderStatus = order.getOrderStatus();
	}
}
