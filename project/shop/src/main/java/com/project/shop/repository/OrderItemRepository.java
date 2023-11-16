package com.project.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.shop.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
