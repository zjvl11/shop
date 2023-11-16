package com.project.shop.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.project.shop.domain.OrderDTO;
import com.project.shop.domain.OrderHistDTO;
import com.project.shop.domain.OrderItemDTO;
import com.project.shop.entity.Item;
import com.project.shop.entity.ItemImg;
import com.project.shop.entity.Member;
import com.project.shop.entity.Order;
import com.project.shop.entity.OrderItem;
import com.project.shop.repository.ItemImgRepository;
import com.project.shop.repository.ItemRepository;
import com.project.shop.repository.MemberRepository;
import com.project.shop.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

	private final ItemRepository itemRepository;
	private final MemberRepository memberRepository;
	private final OrderRepository orderRepository;
	private final ItemImgRepository itemImgRepository;

	public Long order(OrderDTO orderDto, String email) {
		Item item = itemRepository.findById(orderDto.getItemId())
						.orElseThrow(EntityNotFoundException::new);
		Member member = memberRepository.findByEmail(email);
		
		List<OrderItem> orderItemList = new ArrayList<>();
		OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
		orderItemList.add(orderItem);
		
		Order order = Order.createOrder(member, orderItemList);
		orderRepository.save(order);
		
		return order.getId();
	}
	
	
	//주문목록 조회
	@Transactional(readOnly = true)
	public Page<OrderHistDTO> getOrderList(String email, Pageable pageable){
		
		List<Order> orders = orderRepository.findOrders(email, pageable); //유저의 아이디와 페이징 조건을 이용하여 주문 목록 조회
		Long totalCount = orderRepository.countOrder(email); //유저의 주문 총 개수 구하기
		
		List<OrderHistDTO> orderHistDtos = new ArrayList<>();
		
		for(Order order : orders) { //주문 리스트를 순회하며 구매 이력 페이지에 전달할 DTO생성
			OrderHistDTO orderHistDto = new OrderHistDTO(order);
			List<OrderItem> orderItems = order.getOrderItems();
			for(OrderItem orderItem : orderItems) {
				ItemImg itemImg = itemImgRepository.findByItemAndRepimgYn(orderItem.getItem().getId(), "Y"); // 주문한 상품의 대표 이미지 조회
				OrderItemDTO orderItemDto = new OrderItemDTO(orderItem, itemImg.getImgUrl());
				orderHistDto.addOrderItemDto(orderItemDto);
			}
			
			orderHistDtos.add(orderHistDto);
		}
		return new PageImpl<OrderHistDTO>(orderHistDtos, pageable, totalCount); // 페이지 구현 객체 생성 반환
	}
	
	//주문 취소 
	@Transactional(readOnly = true)
	public boolean validaterOrder(Long orderId, String eamil) {
		Member curMember = memberRepository.findByEmail(email);
		Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
		
		Member savedMember = order.getMember();
	
		if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
			return false;
		}
		
		return true;
	
	}
	
	
	public void cancelOrder(Long orderId) {
		Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
		order.cancelOrder();
	}
	
	// 장바구니에서 주문할 상품 데이터를 전달받아 주문 생성하는 로직
	public Long orders(List<OrderDTO> orderDtoList, String email) {
		Member member = memberRepository.findByEmail(email);
		List<OrderItem> orderItemList = new ArrayList<>();
		
		for(OrderDTO orderDto : orderDtoList) { // 주문할 상품 리스트를 만들어줌
			Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
			
			OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
			orderItemList.add(orderItem);
		}
		
		Order order = Order.createOrder(member, orderItemList); // 현재 로그인한 회원과 주문 상품 목록을 이용해 주문 엔티티를 만듬
		orderRepository.save(order); // 주문 데이터 저장
		
		return order.getId();
	}
}
