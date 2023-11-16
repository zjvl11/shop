package com.project.shop.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartOrderDTO {

	private Long cartItemId;
	
	private List<CartOrderDTO> cartOrderDtoList; // 장바구니에서 여러 개의 상품을 주문하므로  CartOrderDTO 클래스가 자기 자신을 List로 갖고 있도록!
}
