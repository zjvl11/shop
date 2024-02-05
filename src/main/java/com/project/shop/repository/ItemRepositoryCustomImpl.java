package com.project.shop.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;

import com.project.shop.dto.MainItemDto;
import com.project.shop.dto.QMainItemDto;
import com.project.shop.entity.QItemImg;
import com.querydsl.core.types.dsl.Wildcard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.project.shop.constant.ItemSellStatus;
import com.project.shop.dto.ItemSearchDto;
//import com.project.shop.dto.MainItemDTO;
import com.project.shop.entity.Item;
import com.project.shop.entity.QItem;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private JPAQueryFactory queryFactory;//동적으로 쿼리를 생성하기 위해 JPAQueryFactory 클래스 사용

    public ItemRepositoryCustomImpl(EntityManager em) {//JPAQueryFactory의 생성자로 EntityManager객체를 넣어줌
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
        //상품 판매 상태 조건이 전체(null)일 경우 null리턴. 결과값이 null이면 where절에서 해당 조건은 무시.
        // 상품 판매 상태 조건이 null이 아니라 판매중, 품절 상태라면 해당 조건의 상품만 조회

        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
        // searchDateType의 값에 따라 dateTime의 값을 이전 시간의 값으로 세팅 후 해당 시간 이후로 등록된 상품만 조회.
        // searchDateType값이 "1m"인 경우 dateTime의 시간을 한달전으로 세팅 후 최근 한달 동안 등록된 상품만 조회하도록 조건값 반환
    }

    private BooleanExpression regDtsAfter(String searchDateType) {
        //searchDateType의 값에 따라 dateTime의 값을 이전 시간의 값으로 세팅 후 해당 시간 이후로 등록된 상품만 조회.
        // searchDateType값이 "1m"인 경우 dateTime의 시간을 한달전으로 세팅 후 최근 한달 동안 등록된 상품만 조회하도록 조건값 반환

        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if(StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }
        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        //searchBy의 값에 따라 상품명에 검색어를 포함하고 있는 상품 또는 상품 생성자의 아이디에 검색어를 포함하고 있는 상품을 조회하도록 조건값을 반환

        if(StringUtils.equals("itemName", searchBy)) {
            return QItem.item.itemName.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)) {
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        /*
         * selectFrom(QItem.item) :  상품데이터 조회하기 위해 QItem의 item지정
         * where 조건절 : BooleanExpression 반환하는 조건문들을 넣어줌. '.'단위로 넣어줄 경우 and조건으로 인식
         * offset : 데이터를 가지고 올 시작 인덱스를 지정
         * limit: 한번에 가지고 올 최대 개수 지정
         * fetchResult() : 조회한 리스트 및 전체 개수를 포함하는 QueryResults를 반환합니다. 상품 데이터 리스트 조회 및 상품 데이터 전체 개수를 조회하는 2번의 쿼리문이 실행된다.
         *
         *
         * */
        List<Item> content = queryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(),
                                itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);// 조회한 데이터를 Page 클래스의 구현체인 PageImpl 객체로 반환
    }

    private BooleanExpression itemNameLike(String searchQuery) { //검색어가 null이 아니면 상품명에 해당 검색어가 포함되는 상품을 조회하는 조건을 반환
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemName.like("%" + searchQuery + "%");
    }

    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){

        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainItemDto> content = queryFactory
                .select(
                        new QMainItemDto( //QMainItemDTO의 생성장[ 반환할 값들을 넣어줌. @QueryProjection을 사용하면 DTO로 바로 조회가능하다. 엔티티 조회 후 DTO로 변환하는 과정을 줄일 수 있음
                                item.id,
                                item.itemName,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price)
                )
                .from(itemImg)
                .join(itemImg.item, item) //조인!
                .where(itemImg.repimgYn.eq("Y")) // 상품 이미지의 경우 대표 상품 이미지만 불러옵니다.
                .where(itemNameLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNameLike(itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);

    }

}