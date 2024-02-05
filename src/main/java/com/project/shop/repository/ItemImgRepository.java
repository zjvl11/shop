package com.project.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.shop.entity.ItemImg;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long>{

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);
//
    ItemImg findByItemAndRepimgYn(Long itemId, String repimgYn);

}