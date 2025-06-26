package com.study.coffee.point.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.study.coffee.exception.PointChargeException;
import com.study.coffee.point.domain.PointDTO;
import com.study.coffee.point.repository.PointRepository;

@SpringBootTest
class PointApplicationTests {

	private PointDTO testPoint;
	
	@Autowired
	private PointRepository pointRepository;
	//각 테스트 진행 전 포인트 데이터 세팅 후 진행
	@BeforeEach
	void setUp() {
		testPoint = new PointDTO();
		testPoint.setUserId("testUser");
		testPoint.setPoint(2000);
	}
	
	/**
		2. 포인트 충전 하기 API
    		- 결제는 포인트로만 가능하며, 포인트를 충전하는 API를 작성합니다.
    		- 사용자 식별값, 충전금액을 입력 받아 포인트를 충전합니다. (1원=1P)
	 */
	@Test
	void 존재하는_유저인지_체크() {
		//Given
			PointRepository mock = Mockito.mock(PointRepository.class);
			Mockito.when(mock.countByUserId(testPoint.getUserId())).thenReturn(1);
	
		//When
			PointService pointService = new PointService(mock);
			boolean isExistUser = pointService.isExistUser(testPoint.getUserId());
			
		//Then
			Mockito.verify(mock,atLeastOnce()).countByUserId(testPoint.getUserId());
			assertEquals(true,isExistUser);
	}
	
	@Test
	void 유저가_존재하지_않는_경우() {
		//Given
//			PointRepository mock = Mockito.mock(PointRepository.class);
//			Mockito.when(mock.countByUserId(testPoint.getUserId())).thenReturn(0);
	
		//When
			PointService pointService = new PointService(pointRepository);
			boolean isExistUser = pointService.isExistUser("aaaa");
			
		//Then
//			Mockito.verify(mock,atLeastOnce()).countByUserId(testPoint.getUserId());
			assertEquals(false,isExistUser);
	}
	
	@Test
	void 잔여_포인트_조회() {
		//Given
//			PointRepository mock = Mockito.mock(PointRepository.class);
//			Mockito.when(mock.findByUserId(testPoint.getUserId()).get()).thenReturn(testPoint);
		//When
			PointService pointService = new PointService(pointRepository);
			int remainingPoint = pointService.getRemainingPoint(testPoint.getUserId());
		//Then
			assertEquals(testPoint.getPoint(), remainingPoint);
	}

	@Test//유저는 존재한다고 가정
	void 포인트_충전_성공() throws PointChargeException {
		//Given
			int remainingPoint = testPoint.getPoint();
			int chargePoint = 1000;
			
			int totalPoint = remainingPoint + chargePoint;
			
		//When
			PointService pointService = new PointService(pointRepository);
			chargePoint = pointService.chargePoint(chargePoint,remainingPoint,testPoint.getUserId());
			
		//Then
			assertEquals(totalPoint, chargePoint);
	}
	
	@Test//유저는 존재한다고 가정
	void 포인트_충전_실패() throws PointChargeException {
		//Given
		int remainingPoint = testPoint.getPoint();
		int chargePoint = 1000;
		
		int totalPoint = remainingPoint + chargePoint;
		
		PointDTO pointDTO = PointDTO.builder().userId("testUser").point(totalPoint).build();
		
		PointRepository mock = Mockito.mock(PointRepository.class);
		Mockito.when(mock.save(pointDTO)).thenThrow(PointChargeException.class);
		
		//When
		PointChargeException exception = assertThrows(PointChargeException.class,
											() -> new PointService(mock).chargePoint(chargePoint,remainingPoint, testPoint.getUserId()));
		
		//Then
		assertEquals("Point Charging Error, Remaining Point: 2000", exception.getMessage());
	}


	@Test//유저는 존재한다고 가정
	void 포인트_충전_실패_충전요청_포인트가_0인_경우() throws PointChargeException {
		//Given
			int remainingPoint = testPoint.getPoint();
			int zeroChargePoint = 0;
			
			int zeroTotalPoint = remainingPoint + zeroChargePoint;
			
//			PointDTO pointDTO = PointDTO.builder().userId("testUser").point(zeroChargePoint).build();
//			
//			PointRepository mock = Mockito.mock(PointRepository.class);
//			Mockito.when(mock.save(pointDTO)).thenReturn(pointDTO);
			
		//When
			PointService pointService = new PointService(pointRepository);
			zeroTotalPoint = pointService.chargePoint(zeroChargePoint,remainingPoint,testPoint.getUserId());
		
		//Then
			assertTrue(zeroTotalPoint == remainingPoint);
	}
	
	@Test//유저는 존재한다고 가정
	void 포인트_충전_실패_충전요청_포인트가_0보다_작은_경우() throws PointChargeException {
		//Given
			int remainingPoint = testPoint.getPoint();
			int minusChargePoint = -1000;
			
			int minusTotalPoint = remainingPoint + minusChargePoint;
//			
//			PointDTO pointDTO = PointDTO.builder().userId("testUser").point(minusTotalPoint).build();
//			
//			PointRepository mock = Mockito.mock(PointRepository.class);
//			Mockito.when(mock.save(pointDTO)).thenReturn(pointDTO);
		
		//When
			PointService pointService = new PointService(pointRepository);
			minusTotalPoint = pointService.chargePoint(minusChargePoint,remainingPoint,testPoint.getUserId());
		
		//Then
			assertTrue(minusTotalPoint < remainingPoint);
	}
}
