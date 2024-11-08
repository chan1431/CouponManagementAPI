package com.couponmanagement.Services;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.couponmanagement.Entity.Cart;
import com.couponmanagement.Entity.Coupon;
import com.couponmanagement.Entity.Item;
import com.couponmanagement.Exceptions.CouponNotFoundException;
import com.couponmanagement.Exceptions.NoGetProductsException;
import com.couponmanagement.Exceptions.TypeMissMatchException;
import com.couponmanagement.Repository.CouponRepository;

@Service
public class CouponServices {
	
	@Autowired
	private CouponRepository couponRepo;
	
	
	private HashMap<String, Object> convertCouponToMap(Coupon coupon) {
	
		
		HashMap<String, Object> couponMap=new HashMap<>();
		
		for(Field field: Coupon.class.getDeclaredFields())
		{
			field.setAccessible(true);
			try {
				Object value=field.get(coupon);
				if(value!=null)
				{
					couponMap.put(field.getName(), value);
				}
			}
			catch(Exception e)
			{
				throw new RuntimeException(e);
			}
		}
		
		return couponMap;
	}
	
	
	public HashMap<String, Object> addCouponToRepo(Coupon coupon) {
		
		return convertCouponToMap(couponRepo.save(coupon));
	}


	public List<HashMap<String, Object>> getAllCouponsFromRepo() {
		
		List<HashMap<String,Object>> coupons=new ArrayList<>();
		couponRepo.findAll().forEach(coupon -> coupons.add(convertCouponToMap(coupon)));
		
		
		return coupons;	
	}
	public HashMap<String, Object> getSpecificCouponFromRepo(Long id) {
		
		HashMap<String,Object> specificCoupon=new HashMap<>();
		specificCoupon=convertCouponToMap(couponRepo.findById(id)
													.orElseThrow(()-> new CouponNotFoundException("Coupon Doesnt Exiset with id : "+id)));
		
		return specificCoupon;	
	}
	public HashMap<String, Object> updateSpecificCouponFromRepo(Long id,Coupon updatedCoupon) {
		
		Coupon currentCoupon=couponRepo.findById(id)
				.orElseThrow(()-> new CouponNotFoundException("Coupon Doesnt Exiset with id : "+id));
		if(currentCoupon.getType().equals(updatedCoupon.getType()))
		{
		for(Field field:Coupon.class.getDeclaredFields())
		{
			field.setAccessible(true);
			try {
				Object value=field.get(updatedCoupon);
				if(value!=null && field.get(currentCoupon)!=null)
				{
					field.set(currentCoupon, value);
				}
				
			}
			catch(Exception e)
			{
				throw new RuntimeException("Update Error "+e);
			}
		}
		
			
		}
		else {
			throw new TypeMissMatchException("Coupon type doesnt match the existing coupon");
		}
		return addCouponToRepo(currentCoupon);
	}
	public void deleteSpecificCouponfromRepo(Long id) {
		
		getSpecificCouponFromRepo(id);
		
		couponRepo.deleteById(id);
		
	}
	private void caluculateCartTotal(Cart cart) {
		// TODO Auto-generated method stub
		
		cart.getItems().forEach(item -> cart.setTotal(cart.getTotal()+item.getQuantity()*item.getPrice()));
		
	}	
	public List<HashMap<String,Object>> getAllApplicableCoupons(Cart cart)
	{
		caluculateCartTotal(cart);
		
		List<HashMap<String,Object>> applicableCoupons=new ArrayList<>();
		Map<Integer, Integer>cartProductIdQuantity=cart.getItems().stream()
																 .collect(Collectors.toMap(Item::getProductId, Item::getQuantity));
		couponRepo.findAll().stream().filter(coupon -> coupon.getType().equals("cart-wise"))
									 .filter(coupon -> cart.getTotal() >= coupon.getTreshold() )
									 .collect(Collectors.toList())
									 .forEach(coupon -> applicableCoupons.add(convertCouponToMap(coupon)));
		couponRepo.findAll().stream().filter(coupon -> coupon.getType().equals("product-wise"))
		 							 .filter(coupon -> cart.getItems().stream().anyMatch(item -> item.getProductId().equals(coupon.getProductId())))
		 							 .collect(Collectors.toList())
		 							 .forEach(coupon -> applicableCoupons.add(convertCouponToMap(coupon)));
		couponRepo.findAll().stream().filter(coupon -> coupon.getType().equals("BxGy"))
		 							 .filter(coupon -> coupon.getBuyProducts().stream()
		 									 .anyMatch(productId -> 
		 									 cartProductIdQuantity.containsKey(productId)
		 									 &&
		 									 cartProductIdQuantity.get(productId)>=(coupon.getBuyQuantity())
		 									 ))
		 							 .collect(Collectors.toList())
		 							.forEach(coupon -> applicableCoupons.add(convertCouponToMap(coupon)));
		
		
		return applicableCoupons;
		
	}
	
	public HashMap<String,Object> applyCoupon(Long id,Cart cart)
	{
		double finalPrice=0;
		HashMap<String,Object> updatedCart=new HashMap<>();
		Map<Integer, Integer>cartProductIdQuantity=cart.getItems().stream()
				 .collect(Collectors.toMap(Item::getProductId, Item::getQuantity));
		Map<Integer, Integer>cartProductIdPrice=cart.getItems().stream()
				 .collect(Collectors.toMap(Item::getProductId, Item::getPrice));
		
		if(getAllApplicableCoupons(cart).stream().anyMatch(object -> object.containsValue(id) ))
		{
			Coupon coupon=couponRepo.findById(id)
									.orElseThrow(()-> new CouponNotFoundException("Coupon with id doesnt exist : "+id));
			if(coupon.getType().equals("cart-wise"))
			{
				finalPrice=cart.getTotal()-cart.getTotal()*coupon.getDiscount()/100;
			}
			if(coupon.getType().equals("product-wise"))
			{
				finalPrice=cart.getTotal()-
						cartProductIdQuantity.get(coupon.getProductId())*cartProductIdPrice.get(coupon.getProductId())
						*coupon.getDiscount()/100;
			}
			if(coupon.getType().equals("BxGy") )
			{
				Integer getProductId=coupon.getGetProducts().stream()
						.filter(productId -> cartProductIdQuantity.containsKey(productId))
						.findFirst().orElseThrow(() -> new NoGetProductsException("No Get Products Added To Cart"));
				finalPrice=cart.getTotal()-cartProductIdQuantity.get( getProductId)*cartProductIdPrice.get(getProductId);
			}
			
			
		}
		updatedCart.put("finalPrice",finalPrice);
		updatedCart.put("cart",cart);
		
		
		return updatedCart;
	}




	

}
