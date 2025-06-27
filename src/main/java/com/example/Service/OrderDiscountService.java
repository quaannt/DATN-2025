package com.example.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.DiscountCode;
import com.example.Entity.OrderDiscount;
import com.example.Repository.OrderDiscountRepository;

@Service
public class OrderDiscountService {

	@Autowired
	OrderDiscountRepository orderDiscountRepository;
	
	OrderDiscount findById(int id) {
		return orderDiscountRepository.findById(id).orElse(null);
	}
	
	public void save(OrderDiscount orderDiscount) {
		orderDiscountRepository.save(orderDiscount);
	}
	
	public void createOrderDiscount(OrderDiscount orderDiscount, DiscountCode discountCode) {
		orderDiscount.setCode(discountCode.getCode());
		orderDiscount.setDiscountValue(discountCode.getDiscountValue());
		save(orderDiscount);
	}
}
