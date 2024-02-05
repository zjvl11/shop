package com.project.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.project.shop.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long>,
		QuerydslPredicateExecutor<Item>, ItemRepositoryCustom{

	//상품명을 이용하여 데이터를 조회
	List<Item> findByItemName(String itemName);
	
	//상품명과 상품 상세 설명을 OR 조건이용한 조회
	List<Item> findByItemNameOrItemDetail(String itemName, String itemDetail);

	//LessThan 조건 처리
	List<Item> findByPriceLessThan(Integer price);

	@Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
	List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

	@Query(value="select * from item i where i.item_detail like " +
			"%:itemDetail% order by i.price desc", nativeQuery = true)
	List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);

}