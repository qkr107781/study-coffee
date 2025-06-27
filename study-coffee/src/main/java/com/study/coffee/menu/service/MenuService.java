package com.study.coffee.menu.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.study.coffee.menu.domain.MenuDTO;
import com.study.coffee.menu.repository.MenuRepository;

@Service
public class MenuService {

	private final MenuRepository menuRepository;
	
	public MenuService(MenuRepository menuRepository) {
		this.menuRepository = menuRepository;
	}
	
	public List<MenuDTO> getAllMenuList(){
		return menuRepository.findAll();
	}
	
	public MenuDTO getMenu(String menuid) {
		return menuRepository.findByMenuid(menuid);
	}
}
