package com.study.coffee.menu.domain;


import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)//now()로 LocalDateTime 컬럼 default_value 입력
@Table(name="std_menu")
@Getter
@NoArgsConstructor
public class MenuDTO {

	@Builder
	public MenuDTO(String menuid, String name, int price) {
		this.menuid = menuid;
		this.name = name;
		this.price = price;
	}
	
	@Id
	@Column(name="menuid",columnDefinition = "VARCHAR(20)")
	private String menuid;
	
	@Column(name="name",columnDefinition = "VARCHAR(20)")
	private String name;
	
	@Column(name="price",columnDefinition = "INT")
	private int price;
	
}
