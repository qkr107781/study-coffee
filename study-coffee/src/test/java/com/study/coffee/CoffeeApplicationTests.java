package com.study.coffee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.study.coffee.domain.Coffee;
import com.study.coffee.repository.CoffeeRepository;
import com.study.coffee.service.CoffeeService;

@SpringBootTest
class CoffeeApplicationTests {

	/**
	 * 1. 커피 메뉴 목록 조회 API
			- 커피 정보(메뉴ID, 이름, 가격)을 조회하는 API를 작성합니다.
	 */
	@Test
	void 커피_목록_조회() {
		//Given
		//비교할 결과값 세팅
		Coffee icsAmericano = new Coffee();
		icsAmericano.setMenuId("1");
		icsAmericano.setName("icsAmericano");
		icsAmericano.setPrice(4000);
		Coffee water = new Coffee();
		water.setMenuId("2");
		water.setName("water");
		water.setPrice(2000);

		List<Coffee> resultList = new ArrayList<Coffee>();
		resultList.add(icsAmericano);
		resultList.add(water);

		System.out.println("Result List");
		for (Coffee coffee : resultList) {
			System.out.println("\t"+coffee.getMenuId()+":"+coffee.getName()+":"+coffee.getPrice());
		}

		//mock 객체로 DB조회 값 세팅
		CoffeeRepository mockRepo = mock(CoffeeRepository.class);
		when(mockRepo.getCoffeeList()).thenReturn(resultList);

		//When
		//비지니스 로직에서 커피 목록 조회하는 메소드 호출
		CoffeeService coffeeService = new CoffeeService(mockRepo);
		List<Coffee> coffeeList = coffeeService.getCoffeeList();

		System.out.println("Coffee List");
		for (Coffee coffee : coffeeList) {
			System.out.println("\t"+coffee.getMenuId()+":"+coffee.getName()+":"+coffee.getPrice());
		}
		//Then
		assertEquals(resultList,coffeeList);
	}

}
