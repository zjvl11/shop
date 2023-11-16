package com.project.shop.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDTO {

	private Long id;
	
	private String itemName;
	
	private Integer price;
	
	private String itemDetail;
	
	private String sellStatCd;
	
	private LocalDateTime regTime;
	
	private LocalDateTime updateTime;
	
}
