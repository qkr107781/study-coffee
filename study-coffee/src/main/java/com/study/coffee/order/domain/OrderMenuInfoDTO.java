package com.study.coffee.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="order_menu_info")
@Getter
@NoArgsConstructor
public class OrderMenuInfoDTO {

	@Builder
	public OrderMenuInfoDTO(String ukey, String menuid, String orderkey, int price) {
		this.ukey = ukey;
		this.menuid = menuid;
		this.orderkey = orderkey;
		this.price = price;
	}

	@Id
	@Column(name="ukey",columnDefinition = "INT")
	@GeneratedValue(strategy = GenerationType.IDENTITY) //DB에 설정된 방식으로 기본키 생성
	private String ukey;
	
	@Column(name="menuid",columnDefinition = "VARCHAR(20)")
	private String menuid;
	
	//3개 메뉴 주문 시 row 3개를 하나의 주문으로 인식하기 위한 키 
	@Column(name="orderkey",columnDefinition = "VARCHAR(20)")
	private String orderkey;
	
	@Column(name="price",columnDefinition = "INT")
	private int price;
	
}
