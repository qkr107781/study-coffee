package com.study.coffee.order.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import com.study.coffee.menu.domain.MenuDTO;
import com.study.coffee.menu.repository.MenuRepository;
import com.study.coffee.order.domain.OrderInfoDTO;
import com.study.coffee.order.repository.OrderInfoRepository;
import com.study.coffee.order.repository.OrderMenuInfoRepository;
import com.study.coffee.point.domain.PointDTO;
import com.study.coffee.point.repository.PointRepository;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@SpringBootTest
class OrderApplicationTests {
	
	
	@Autowired
	private MenuRepository menuRepo;
	
	@Autowired
	private PointRepository pointRepo;
	
	@Autowired
	private OrderInfoRepository orderInfoRepo;
	
	@Autowired
	private OrderMenuInfoRepository orderMenuInfoRepo;
	
	@BeforeEach
	void setData() {
		 menuRepo.deleteAll();
		 pointRepo.deleteAll();
		 
		 MenuDTO menu1 = MenuDTO.builder().menuid("1").name("americano").price(4000).build(); 
		 MenuDTO menu2 = MenuDTO.builder().menuid("2").name("water").price(2000).build(); 
		 MenuDTO menu3 = MenuDTO.builder().menuid("3").name("juice").price(8000).build(); 
		 MenuDTO menu4 = MenuDTO.builder().menuid("4").name("cake").price(6000).build(); 
		 MenuDTO menu5 = MenuDTO.builder().menuid("5").name("sandwich").price(12000).build();
		 
		 menuRepo.save(menu1);
		 menuRepo.save(menu2);
		 menuRepo.save(menu3);
		 menuRepo.save(menu4);
		 menuRepo.save(menu5);
		 
		 PointDTO point = PointDTO.builder().userId("testUser").point(20000).build();
		 
		 pointRepo.save(point);
	}
	
	String uekyUUID = "testUserOrder";

	
	@Test
	@DisplayName("주문 메뉴가 없을때")
	void saveOrderFail() {
		//주문 메뉴 세팅
		String userid = "testUser";
		String[] menuIds = {"6","7"};
		
		OrderService orderService = new OrderService(orderInfoRepo,orderMenuInfoRepo,menuRepo,pointRepo);
		
		//단수 혹은 복수의 메뉴선택 후 주문 요청
		OrderInfoDTO orderInfo = orderService.saveOrder(userid,menuIds);
		
		assertNull(orderInfo);
	}
	
	@Test
	@DisplayName("주문 완료-결제 전")
	void saveOrder() {
		//주문 메뉴 세팅
		String userid = "testUser";
		String[] menuIds = {"1","2"};
		
		OrderService orderService = new OrderService(orderInfoRepo,orderMenuInfoRepo,menuRepo,pointRepo);
		
		//단수 혹은 복수의 메뉴선택 후 주문 요청
		OrderInfoDTO orderInfo = orderService.saveOrder(userid,menuIds);

		assertEquals("testUser", orderInfo.getUserid());
		assertEquals(6000, orderInfo.getTotalprice());
		
//		uekyUUID = orderInfo.getUkey();
	}
	
	@Test
	@DisplayName("주문 완료-결제 완료")
	void orderPayment() {
		//주문 메뉴 세팅
		Optional<OrderInfoDTO> order = Optional.of(OrderInfoDTO.builder()
													.ukey(uekyUUID)
													.userId("testUser")
													.totalprice(6000)
													.orderstatus("N")
													.regdate(LocalDateTime.now())
													.paydate(null)
													.build());
		
		//주문상태 체크하여 결제 요청
		//결제 완료 시 주문상태 및 결제일자 업데이트
		OrderService orderService = new OrderService(orderInfoRepo,orderMenuInfoRepo,menuRepo,pointRepo);
		boolean payResult = orderService.orderPayment(order.get());
		
		assertTrue(payResult);
	}
	
}
