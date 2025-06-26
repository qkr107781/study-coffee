package com.study.coffee.order.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.study.coffee.menu.domain.MenuDTO;
import com.study.coffee.menu.repository.MenuRepository;
import com.study.coffee.order.domain.OrderInfoDTO;
import com.study.coffee.order.domain.OrderMenuInfoDTO;
import com.study.coffee.order.repository.OrderRepository;
import com.study.coffee.point.domain.PointDTO;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final MenuRepository menuRepository;
	private final EntityManager em;
	
	public OrderService(OrderRepository orderRepository, MenuRepository menuRepository, EntityManager em) {
		this.orderRepository = orderRepository;
		this.menuRepository = menuRepository;
		this.em = em;
	}

	//유저식별값으로 존재하는 유저인지 체크 -> 컨트롤러에서 체크
	//단수 혹은 복수의 메뉴선택 후 주문 요청
	//주문 완료 후 소유 포인트 체크하여 결제 금액보다 모자른 경우 결제 불가 -> 포인트 충전으로 이동 -> 컨트롤러에서 체크
	//주문 완료 후 소유 포인트 부족 상태에서 충전하지 않고 종료 -> 컨트롤러에서 체크
	public OrderInfoDTO saveOrder(String userid, String[] menuIds) {
		String orderUkey = "1";
		int totalPrice = 0;
		String orderStatus = "N";
		
		
		//입력받은 menuIds로 메뉴 가격 조회하여 세팅
		List<OrderMenuInfoDTO> orderMenuInfoList = new ArrayList<>();
		for (int i = 0; i < menuIds.length; i++) {
			if(StringUtils.isEmpty(menuIds[i])) {
				return null;
			}
			int price = menuRepository.findById(menuIds[i]).map(MenuDTO::getPrice).get();
			
			OrderMenuInfoDTO orderMenuInfo =  OrderMenuInfoDTO.builder()
															.ukey(Integer.toString(i))
															.menuid(menuIds[i])
															.orderkey(orderUkey)
															.price(price)
															.build();
			orderMenuInfoList.add(orderMenuInfo);
			
			totalPrice += menuRepository.findById(menuIds[i]).map(MenuDTO::getPrice).get();
		}
		
		OrderInfoDTO order = OrderInfoDTO.builder()
										.ukey(orderUkey)
										.userId(userid)
										.totalprice(totalPrice)
										.orderstatus(orderStatus)
										.regdate(LocalDateTime.now())
										.paydate(null)
										.orderMenuInfo(orderMenuInfoList)
										.build();
		
		return orderRepository.save(order);
	}
	  
	//주문상태 체크하여 결제 요청
	//결제 완료 시 주문상태 및 결제일자 업데이트
	@Transactional
	public boolean orderPayment(OrderInfoDTO orderInfoDTO) {
		String userId = orderInfoDTO.getUserid();
		String orderStatus = orderInfoDTO.getOrderstatus();
		int totalPrice = orderInfoDTO.getTotalprice();
		
		if("N".equals(orderStatus)) {
			PointDTO pointDTO = em.find(PointDTO.class, userId); 
			
			int userPoint = pointDTO.getPoint();
			int remainigPoint = totalPrice - userPoint;
			if(remainigPoint < 0) {
				return false;
			}else {
				pointDTO.updatePoint(remainigPoint);
			}
			
			OrderInfoDTO order = em.find(OrderInfoDTO.class, orderInfoDTO.getUkey());
			order.updateOrderStatus("Y");
			order.updatePaydate(LocalDateTime.now());
		}
		
		return true;
	}
	
}
