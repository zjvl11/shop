package com.project.shop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.project.shop.constant.ItemSellStatus;

import com.project.shop.dto.ItemFormDto;
import com.project.shop.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity{

	/** 상품 코드*/
	@Id
	@Column(name="item_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/** 상품명*/
	@Column(nullable = false, length = 50)
	private String itemName;
	
	/** 가격*/
	@Column(name="price", nullable = false)
	private int price;
	
	/** 재고 수량*/
	@Column(nullable = false)
	private int stockNumber;
	
	/** 상품 상세 설명*/
	@Lob
	@Column(nullable = false)
	private String itemDetail;
	
	
	/** 상품 판매 상태*/
	@Enumerated(EnumType.STRING)
	private ItemSellStatus itemSellStatus;

	public void updateItem(ItemFormDto itemFormDto) {
		this.itemName = itemFormDto.getItemName();
		this.price = itemFormDto.getPrice();
		this.stockNumber = itemFormDto.getStockNumber();
		this.itemDetail = itemFormDto.getItemDetail();
		this.itemSellStatus = itemFormDto.getItemSellStatus();
	}

	//재고 감소
	public void removeStock(int stockNumber) {
		int restStock = this.stockNumber - stockNumber; // 상품의 재고수량에서 주문 후 남은 재고 수량 구하기
		if(restStock < 0) {
			throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량 : " + this.stockNumber + ")");
			// 상품의 재고가 주문 수량보다 작을 경우 재고 부족 예외를 발생
		}
		this.stockNumber = restStock; // 주문 후 남은 재고 수량을 상품의 현재 재고 값으로 할당
	}

	//재고 증가 (주문을 취소할 경우)
	public void addStock(int stockNumber) {
		this.stockNumber += stockNumber;
	}
}
