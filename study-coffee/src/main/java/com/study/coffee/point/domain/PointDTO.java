package com.study.coffee.point.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@EntityListeners(AuditingEntityListener.class)//now()로 LocalDateTime 컬럼 default_value 입력
@Table(name="std_point")
@Getter
@Setter
@NoArgsConstructor
public class PointDTO {

	@Builder
	public PointDTO(String userId, int point) {
		this.userId = userId;
		this.point = point;
	}
	
	@Id
	@Column(name="userid",columnDefinition = "VARCHAR(20)")
	private String userId;
	
	@Column(name="point",columnDefinition = "INT")
	private int point;
	
	public void updatePoint(int point) {
		this.point = point;
	}

}
