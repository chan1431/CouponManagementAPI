package com.couponmanagement.Exceptions;



@SuppressWarnings("serial")
public class CouponNotFoundException extends RuntimeException {
	
	public CouponNotFoundException(String msg)
	{
		super(msg);
	}

}
