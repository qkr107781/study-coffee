package com.study.coffee.menu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.coffee.menu.domain.MenuDTO;
import com.study.coffee.menu.service.MenuService;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
	
	@Autowired
	private MenuService menuService;
	
	@GetMapping("/list")
	public List<MenuDTO> getAllMenuList(){
		List<MenuDTO> resultList = menuService.getAllMenuList();
		return resultList;
	}

}
