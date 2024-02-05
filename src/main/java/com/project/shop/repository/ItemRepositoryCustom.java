package com.project.shop.repository;

import com.project.shop.dto.MainItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.shop.dto.ItemSearchDto;
import com.project.shop.entity.Item;

public interface ItemRepositoryCustom {

    //상품 조회 조건을 담고 있는 itemSearchDto 객체와 페이징 정보를 담고 있는 pageable 객체를 파라미터로 받는 getAdminItemPage 메소드를 정의 . 반환 데이터로 Page<Item> 객체를 반환
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    //메인페이지에 보여줄 상품 리스트를 가져오는 메소드 생성
    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}