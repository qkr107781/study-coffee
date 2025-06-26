package com.study.coffee.order.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.study.coffee.menu.repository.MenuRepository;
import com.study.coffee.order.domain.OrderInfoDTO;
import com.study.coffee.order.domain.OrderMenuInfoDTO;
import com.study.coffee.order.repository.OrderRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class OrderApplicationTests {

	@Autowired
	private EntityManager em;
	
	@Test
	@DisplayName("주문 완료-결제 전")
	void saveOrder() {
		//주문 메뉴 세팅
		List<OrderMenuInfoDTO> orderMenuInfoList = new ArrayList<>();
		orderMenuInfoList.add(OrderMenuInfoDTO.builder()
												.ukey("a")
												.menuid("1")
												.orderkey("1")
												.price(4000)
												.build());
		orderMenuInfoList.add(OrderMenuInfoDTO.builder()
												.ukey("a")
												.menuid("2")
												.orderkey("1")
												.price(2000)
												.build());
		OrderInfoDTO order = OrderInfoDTO.builder()
											.ukey("1")
											.userId("testUser")
											.totalprice(12000)
											.orderstatus("N")
											.regdate(LocalDateTime.now())
											.paydate(null)
											.orderMenuInfo(orderMenuInfoList)
											.build();
		
		OrderRepository orderMock = Mockito.mock(OrderRepository.class);
		MenuRepository menuMock = Mockito.mock(MenuRepository.class);
		Mockito.when(orderMock.save(order)).thenReturn(order);
		
		OrderService orderService = new OrderService(orderMock,menuMock,em);
		
		//단수 혹은 복수의 메뉴선택 후 주문 요청
		String[] menuIds = new String[order.getOrderMenuInfo().size()];
		for (int i = 0; i < menuIds.length; i++) {
			menuIds[i] = order.getOrderMenuInfo().get(i).getMenuid();
		}
		OrderInfoDTO orderInfo = orderService.saveOrder(order.getUserid(),menuIds);

		
		assertEquals(orderMock.save(order).getUkey(), orderInfo.getUkey());
		assertEquals(orderMock.save(order).getUserid(), orderInfo.getUserid());
		assertEquals(orderMock.save(order).getTotalprice(), orderInfo.getTotalprice());
	}
	@Test
	@DisplayName("주문 메뉴가 없을때")
	void saveOrderFail() {
		//주문 메뉴 세팅
		List<OrderMenuInfoDTO> orderMenuInfoList = new ArrayList<>();
		orderMenuInfoList.add(OrderMenuInfoDTO.builder()
				.ukey("a")
				.menuid("")
				.orderkey("1")
				.price(4000)
				.build());
		orderMenuInfoList.add(OrderMenuInfoDTO.builder()
				.ukey("a")
				.menuid("")
				.orderkey("1")
				.price(2000)
				.build());
		OrderInfoDTO order = OrderInfoDTO.builder()
				.ukey("1")
				.userId("testUser")
				.totalprice(12000)
				.orderstatus("N")
				.regdate(LocalDateTime.now())
				.paydate(null)
				.orderMenuInfo(orderMenuInfoList)
				.build();
		
		OrderRepository orderMock = Mockito.mock(OrderRepository.class);
		MenuRepository menuMock = Mockito.mock(MenuRepository.class);
		Mockito.when(orderMock.save(order)).thenReturn(order);
		
		OrderService orderService = new OrderService(orderMock,menuMock,em);
		
		//단수 혹은 복수의 메뉴선택 후 주문 요청
		String[] menuIds = new String[order.getOrderMenuInfo().size()];
		for (int i = 0; i < menuIds.length; i++) {
			menuIds[i] = order.getOrderMenuInfo().get(i).getMenuid();
		}
		OrderInfoDTO orderInfo = orderService.saveOrder(order.getUserid(),menuIds);
		
		assertNull(orderInfo);
	}
	
	@Test
	@DisplayName("주문 완료-결제 완료")
	void orderPayment() {
		//주문 메뉴 세팅
		List<OrderMenuInfoDTO> orderMenuInfoList = new ArrayList<>();
		orderMenuInfoList.add(OrderMenuInfoDTO.builder()
				.ukey("a")
				.menuid("")
				.orderkey("1")
				.price(4000)
				.build());
		orderMenuInfoList.add(OrderMenuInfoDTO.builder()
				.ukey("a")
				.menuid("")
				.orderkey("1")
				.price(2000)
				.build());
		Optional<OrderInfoDTO> order = Optional.of(OrderInfoDTO.builder()
													.ukey("1")
													.userId("testUser")
													.totalprice(40000)
													.orderstatus("N")
													.regdate(LocalDateTime.now())
													.paydate(null)
													.orderMenuInfo(orderMenuInfoList)
													.build());
		
		OrderRepository orderMock = Mockito.mock(OrderRepository.class);
		MenuRepository menuMock = Mockito.mock(MenuRepository.class);
		Mockito.when(orderMock.findById(order.get().getUkey())).thenReturn(order);
		
		//주문상태 체크하여 결제 요청
		//결제 완료 시 주문상태 및 결제일자 업데이트
		OrderService orderService = new OrderService(orderMock, menuMock, em);
		boolean payResult = orderService.orderPayment(order.get());
		
		assertTrue(payResult);
	}
	
}
