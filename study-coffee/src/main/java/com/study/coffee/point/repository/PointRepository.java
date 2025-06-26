package com.study.coffee.point.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.coffee.point.domain.PointDTO;

@Repository
public interface PointRepository extends JpaRepository<PointDTO, String>{
	
	public int countByUserId(String userId);
	public Optional<PointDTO> findByUserId(String userId);
}
