package com.project.shop.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.shop.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByMemberId(Long memberId);
}
