package com.study.coffee.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.coffee.order.domain.OrderInfoDTO;


@Repository
public interface OrderInfoRepository extends JpaRepository<OrderInfoDTO, String>{

	public OrderInfoDTO findByUkey(String ueky);
	
}
