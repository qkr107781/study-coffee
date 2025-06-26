package com.study.coffee.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.coffee.menu.domain.MenuDTO;

@Repository
public interface MenuRepository extends JpaRepository<MenuDTO, String>{
}
