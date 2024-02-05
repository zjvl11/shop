package com.project.shop.dto;

import com.project.shop.constant.ItemSellStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {

    private String searchDateType; // 현재시간과 상품 등록일을 비교해서 상품 데이터를 조회
    /*
     * all : 상품 등록일 전체
     * 1d : 최근 하루 동안 등록된 상품
     * 1w : 최근 일주일 동안 등록된 상품
     * 1m : 최근 한달 동안 등록된 상품
     * 6m : 최근 5개월 동안 등록된 상품
     *
     * */

    private ItemSellStatus searchSellStatus; //상품의 판매상태를 기준으로 상품 데이터를 조회

    private String searchBy; // 상품을 조회할 때 어떤 유형으로 조회할지 선택

    /*
     * itemName : 상품명
     * createdBy : 상품 등록자 아이디
     * */

    private String searchQuery = "";
    //조회할 검색어 저장할 변수. searchBy가 itemName일 경우 상품명을 기준으로 검색하고,
    // createBy일 경우 상품 등록자 아이디 기준으로 검색
}
