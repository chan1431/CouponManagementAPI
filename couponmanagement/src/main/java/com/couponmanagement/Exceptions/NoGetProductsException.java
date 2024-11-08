package com.couponmanagement.Exceptions;

@SuppressWarnings("serial")
public class NoGetProductsException extends RuntimeException {
	
	public NoGetProductsException(String msg)
	{
		super(msg);
	}

}
