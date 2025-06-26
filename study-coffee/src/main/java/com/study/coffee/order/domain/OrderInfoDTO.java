package com.study.coffee.order.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)//now()로 LocalDateTime 컬럼 default_value 입력
@Table(name="std_order_info")
@Getter
@NoArgsConstructor
public class OrderInfoDTO {

	@Builder
	public OrderInfoDTO(String ukey, String userId, int totalprice, String orderstatus, LocalDateTime regdate,
			LocalDateTime paydate, List<OrderMenuInfoDTO> orderMenuInfo) {
		this.ukey = ukey;
		this.userid = userId;
		this.totalprice = totalprice;
		this.orderstatus = orderstatus;
		this.regdate = regdate;
		this.paydate = paydate;
		this.orderMenuInfo = orderMenuInfo;
	}
	
	@Id
	@Column(name="ukey",columnDefinition = "VARCHAR(20)")
	@GeneratedValue(strategy = GenerationType.IDENTITY) //DB에 설정된 방식으로 기본키 생성
	private String ukey;
	
	@Column(name="userid",columnDefinition = "VARCHAR(20)")
	private String userid;
	
	@Column(name="totalprice",columnDefinition = "INT")
	private int totalprice;
	
	//주문 상태: N-결제 전, Y-결제 완료
	@Column(name="orderstatus",columnDefinition = "VARCHAR(1)")
	private String orderstatus;
	
	@Column(name="regdate",columnDefinition = "DATETIME")
	private LocalDateTime regdate;
	
	@Column(name="paydate",columnDefinition = "DATETIME")
	private LocalDateTime paydate;
	
	@OneToMany
	private List<OrderMenuInfoDTO> orderMenuInfo;

	public void updateOrderStatus(String orderstatus) {
		this.orderstatus = orderstatus;
	}
	
	public void updatePaydate(LocalDateTime paydate) {
		this.paydate = paydate;
	}
}
