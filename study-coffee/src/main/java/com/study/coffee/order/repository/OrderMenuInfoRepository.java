package com.study.coffee.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.coffee.order.domain.OrderMenuInfoDTO;


@Repository
public interface OrderMenuInfoRepository extends JpaRepository<OrderMenuInfoDTO, String>{

}
