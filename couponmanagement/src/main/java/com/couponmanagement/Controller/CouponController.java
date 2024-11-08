package com.couponmanagement.Controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.couponmanagement.Entity.Coupon;
import com.couponmanagement.Entity.RequestCart;
import com.couponmanagement.Services.CouponServices;

@Controller
@RequestMapping("/coupons")
public class CouponController {
	
	@Autowired
	private CouponServices couponServ;
	
	@PostMapping
	public ResponseEntity<HashMap<String, Object>> createCoupon(@RequestBody Coupon coupon)
	{
		
		return ResponseEntity.status(HttpStatus.CREATED).body(couponServ.addCouponToRepo(coupon));
	}
	@GetMapping
	public ResponseEntity<List<HashMap<String, Object>>> readAllCoupons()
	{
		
		return ResponseEntity.ok(couponServ.getAllCouponsFromRepo());
	}
	@GetMapping("/{id}")
	public ResponseEntity<HashMap<String, Object>> readSpecificCoupon(@PathVariable Long id)
	{
		
		return ResponseEntity.ok(couponServ.getSpecificCouponFromRepo(id));
	}
	@PutMapping("/{id}")
	public ResponseEntity<HashMap<String, Object>> updateSpecificCoupons(@PathVariable Long id,@RequestBody Coupon coupon)
	{
		
		return ResponseEntity.ok(couponServ.updateSpecificCouponFromRepo(id,coupon));
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteSpecificCoupons(@PathVariable Long id)
	{
		couponServ.deleteSpecificCouponfromRepo(id);
		return ResponseEntity.ok("Coupon Deleted");
	
	}
	@PostMapping("/apply-coupon/{id}")
	public ResponseEntity<HashMap<String,Object>> applyCoupon(@PathVariable Long id,@RequestBody RequestCart requestCart)
	{
		 return ResponseEntity.ok(couponServ.applyCoupon(id,requestCart.getCart()));
		
	}
	@PostMapping("/applicable-coupons")
	public ResponseEntity<List<HashMap<String,Object>>> getAllApplicableCoupons(@RequestBody RequestCart requestCart)
	{
		 return ResponseEntity.ok(couponServ.getAllApplicableCoupons(requestCart.getCart()));
		
	}
	
	

}
