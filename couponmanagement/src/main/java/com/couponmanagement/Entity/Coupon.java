package com.couponmanagement.Entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="coupons")
@Data
public class Coupon {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(nullable=false)
	private String type;
	@Column(nullable=false)
	private Long discount;
	
	@Column(nullable=true)
	private Integer treshold;
	
	@Column(nullable=true)
	private Integer productId;
	
	@Column(nullable=true)
	private List<Integer> getProducts;
	@Column(nullable=true)
	private List<Integer> buyProducts;
	@Column(nullable=true)
	private Integer getQuantity;
	@Column(nullable=true)
	private Integer buyQuantity;

}
