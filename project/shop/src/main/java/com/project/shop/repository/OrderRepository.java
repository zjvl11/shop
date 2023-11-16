package com.project.shop.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.shop.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	//조회 조선이 복잡하지 않아 @Query어노테이션 이용해 구현
	
	@Query("select o from Order o " + 
		   "where o.member.eamil = :email " +
		   "order by o.orderDate desc"
	)
	
	List<Order> findOrders(@Param("eamil") String email, Pageable pageable);
	
	@Query("select count(o) from Order o " + 
		   "where o.member.email = :email"
	)
	Long countOrder(@Param("email") String email);
}
