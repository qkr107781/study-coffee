package com.study.coffee.menu.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atLeastOnce;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.study.coffee.menu.domain.MenuDTO;
import com.study.coffee.menu.repository.MenuRepository;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MenuApplicationTests {

	/**
	 * 1. 커피 메뉴 목록 조회 API
			- 커피 정보(메뉴ID, 이름, 가격)을 조회하는 API를 작성합니다.
	 */
	@Test
	void 커피_목록_조회() {
		//Given
			//모의객체에 사용할 데이터 세팅
			MenuDTO americano = MenuDTO.builder().menuid("1").name("americano").price(4000).build(); 
			MenuDTO water = MenuDTO.builder().menuid("2").name("water").price(2000).build(); 
			List<MenuDTO> resultList = new ArrayList<MenuDTO>();
			resultList.add(americano);
			resultList.add(water);
			
			/**
			 * 1. 모의 객체 생성 : Mock
			 */
			MenuRepository mock = Mockito.mock(MenuRepository.class);
			/**
			 * 2. 메소드 호출 예상 동작 설정 : Stub
			 *     DAO 클래스에서 DB 데이터 읽어오는 동작을 DTO에 미리 세팅해둔 값으로 대체
			 */
			Mockito.when(mock.findAll()).thenReturn(resultList);
		
		//When
			/**
			 * 3. 실제 테스트 대상 메소드 호출
			 */
			MenuService menuService = new MenuService(mock);
			List<MenuDTO> serviceList = menuService.getAllMenuList();
		//Then
			/**
			 * 4. 메소드 호출 검증 : Verify
			 */
			//메소드가 최소 1번 정상적으로 실행 됐는지
			Mockito.verify(mock,atLeastOnce()).findAll();
			//실제 메소드 실행값이 예상 결과값과 같은지
			assertEquals(resultList,serviceList);
	}
	
	@Test
	void 커피_목록이_비어있을때() {
		//Given
			MenuRepository mock = Mockito.mock(MenuRepository.class);
			Mockito.when(mock.findAll()).thenReturn(Collections.emptyList());
		
		//When
			MenuService menuService = new MenuService(mock);
			List<MenuDTO> serviceList = menuService.getAllMenuList();
			
		//Then
			Mockito.verify(mock,atLeastOnce()).findAll();
			assertEquals(Collections.emptyList(),serviceList);
	}
	
	@Test
	void 커피_메뉴_단건_조회() {
		//Given
		MenuDTO americano = MenuDTO.builder().menuid("1").name("americano").price(4000).build(); 
		
		MenuRepository mock = Mockito.mock(MenuRepository.class);
		Mockito.when(mock.findByMenuid("1")).thenReturn(americano);
		
		//When
		MenuService menuService = new MenuService(mock);
		MenuDTO menu = menuService.getMenu("1");
		
		//Then
		Mockito.verify(mock,atLeastOnce()).findByMenuid("1");
		assertEquals(americano,menu);
		
	}

}
