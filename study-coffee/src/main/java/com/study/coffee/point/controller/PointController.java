package com.study.coffee.point.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.coffee.point.service.PointService;

@RestController
@RequestMapping("/api/point")
public class PointController {

	@Autowired
	private PointService pointService;
	
	@GetMapping("/{userId}")
	public int getRemainingPoint(@PathVariable String userId) {
		return pointService.getRemainingPoint(userId);
	}
	
	@PostMapping("/charge")
	public String chargePoint(@RequestParam String chargePoint,@RequestParam String userid){
		boolean isExistUser = pointService.isExistUser(userid);
		String result = ""; 
		if(isExistUser) {
			int remainingPoint = pointService.getRemainingPoint(userid);
			
			int chargeResult = pointService.chargePoint(Integer.parseInt(chargePoint) ,remainingPoint,  userid);
			if(chargeResult > 0) {
				result = "charge success, remaining point: " + chargePoint + "P";
			}
		}
		return result;
	}
	
}
