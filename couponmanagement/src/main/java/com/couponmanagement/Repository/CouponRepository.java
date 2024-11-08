package com.couponmanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.couponmanagement.Entity.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,Long>{

}
