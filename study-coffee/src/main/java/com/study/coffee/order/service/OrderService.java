package com.study.coffee.order.service;

import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.study.coffee.menu.repository.MenuRepository;
import com.study.coffee.order.domain.OrderInfoDTO;
import com.study.coffee.order.domain.OrderMenuInfoDTO;
import com.study.coffee.order.repository.OrderInfoRepository;
import com.study.coffee.order.repository.OrderMenuInfoRepository;
import com.study.coffee.point.domain.PointDTO;
import com.study.coffee.point.repository.PointRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

	private final OrderInfoRepository orderInfoRepository;
	private final OrderMenuInfoRepository orderMenuInfoRepository;
	private final MenuRepository menuRepository;
	private final PointRepository pointRepository;
	
	public OrderService(OrderInfoRepository orderInfoRepository, OrderMenuInfoRepository orderMenuInfoRepository, MenuRepository menuRepository, PointRepository pointRepository) {
		this.orderInfoRepository = orderInfoRepository;
		this.orderMenuInfoRepository = orderMenuInfoRepository;
		this.menuRepository = menuRepository;
		this.pointRepository = pointRepository;
	}

	//유저식별값으로 존재하는 유저인지 체크 -> 컨트롤러에서 체크
	//단수 혹은 복수의 메뉴선택 후 주문 요청
	//주문 완료 후 소유 포인트 체크하여 결제 금액보다 모자른 경우 결제 불가 -> 포인트 충전으로 이동 -> 컨트롤러에서 체크
	//주문 완료 후 소유 포인트 부족 상태에서 충전하지 않고 종료 -> 컨트롤러에서 체크
	public OrderInfoDTO saveOrder(String userid, String[] menuIds) {
//		String orderUkey = UUID.randomUUID().toString();
		String orderUkey = "testUserOrder";
		int totalPrice = 0;
		String orderStatus = "N";
		
		
		//입력받은 menuIds로 메뉴 가격 조회하여 세팅
		for (int i = 0; i < menuIds.length; i++) {
			if(StringUtils.isEmpty(menuIds[i])) {
				return null;
			}
			totalPrice += menuRepository.findByMenuid(menuIds[i]).getPrice();
		}
		
		OrderInfoDTO order = OrderInfoDTO.builder()
										.ukey(orderUkey)
										.userId(userid)
										.totalprice(totalPrice)
										.orderstatus(orderStatus)
										.regdate(LocalDateTime.now())
										.paydate(null)
										.build();
		
		OrderInfoDTO orderResult = orderInfoRepository.save(order);
		
		if(orderResult != null) {
			//입력받은 menuIds로 메뉴 가격 조회하여 세팅
			for (int i = 0; i < menuIds.length; i++) {
				int price =  menuRepository.findByMenuid(menuIds[i]).getPrice();
				OrderMenuInfoDTO orderMenuInfo = OrderMenuInfoDTO.builder()
																.ukey(Integer.toString(i+1))
																.menuid(menuIds[i])
																.price(price)
																.orderInfo(order)
																.build();
				orderMenuInfoRepository.save(orderMenuInfo);
			}
		}
		return orderResult;
	}
	  
	//주문상태 체크하여 결제 요청
	//결제 완료 시 주문상태 및 결제일자 업데이트
	@Transactional
	public boolean orderPayment(OrderInfoDTO orderInfoDTO) {
		String userId = orderInfoDTO.getUserid();
		String orderStatus = orderInfoDTO.getOrderstatus();
		int totalPrice = orderInfoDTO.getTotalprice();
		
		if("N".equals(orderStatus)) {
			System.out.println("결제시작");
			PointDTO pointDTO = pointRepository.findByUserId(userId).get(); 
			
			int userPoint = pointDTO.getPoint();
			int remainigPoint = userPoint - totalPrice;
			if(remainigPoint < 0) {
				return false;
			}else {
				pointDTO.updatePoint(remainigPoint);
			}
			System.out.println("포인트 차감 완료");
			OrderInfoDTO order = orderInfoRepository.findByUkey(orderInfoDTO.getUkey());
			order.updateOrderStatus("Y");
			order.updatePaydate(LocalDateTime.now());
			System.out.println("주문 상태 변경 완료");
		}
		
		return true;
	}
	
}
