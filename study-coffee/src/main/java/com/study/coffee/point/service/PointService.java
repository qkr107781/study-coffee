package com.study.coffee.point.service;

import org.springframework.stereotype.Service;
import com.study.coffee.exception.PointChargeException;
import com.study.coffee.point.domain.PointDTO;
import com.study.coffee.point.repository.PointRepository;

import jakarta.transaction.Transactional;

@Service
public class PointService {

	private final PointRepository pointRepository;
	
	public PointService(PointRepository pointRepository) {
		this.pointRepository = pointRepository;
	}
	
	//잔여 포인트 조회
	public int getRemainingPoint(String userId) {
		return pointRepository.findByUserId(userId).get().getPoint();
	}
	
	//사용자 검증
	public boolean isExistUser(String userId) {
		boolean result = false;
		if(pointRepository.countByUserId(userId) > 0) {
			result = true;
		}
		return result;
	}
	
	//포인트 충전 - 충전 완료 후 잔여 포인트 리턴
	@Transactional
	public int chargePoint(int chargePoint,int remainingPoint, String userId) throws PointChargeException{
		int totalPoint = chargePoint + remainingPoint;
		System.out.println("totalPoint: "+totalPoint);
		System.out.println("paramuserId: "+userId);
		
		try {
			PointDTO point = pointRepository.findByUserId(userId).get();
			
			System.out.println("userId: "+point.getUserId());
			point.updatePoint(totalPoint);
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			throw new PointChargeException("Point Charging Error, Remaining Point: "+remainingPoint);
		}
		
		return totalPoint;
	}
	
}
