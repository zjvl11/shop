package com.project.shop.domain;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDTO {

	private Long id;
	
	private String itemName;
	
	private String itemDetail;
	
	private String imgUrl;
	
	private Integer price;
	
	@QueryProjection
	public MainItemDTO(Long id, String itemName, String itemDetail, String imgUrl, Integer price) {
		
		this.id = id;
		this.itemName = itemName;
		this.itemDetail = itemDetail;
		this.imgUrl = imgUrl;
		this.price = price;
	}
}
