package com.study.coffee.menu.service;

import java.util.List;
import java.util.Optional;

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
	
	public Optional<MenuDTO> getMenu(String menuId) {
		return menuRepository.findById(menuId);
	}
}
