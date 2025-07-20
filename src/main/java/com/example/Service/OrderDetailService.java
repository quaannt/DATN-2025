package com.example.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.OrderDetail;
import com.example.Repository.OrderDetailRepository;

@Service
public class OrderDetailService {

	@Autowired
	OrderDetailRepository orderDetailRepository;
	
	 public OrderDetail findById(int orderDetailId) {
	        Optional<OrderDetail> orderDetailOptional = orderDetailRepository.findById(orderDetailId);
	        return orderDetailOptional.orElse(null);
	    }
	
	public void save(OrderDetail orderDetail) {
		orderDetailRepository.save(orderDetail);
	}
	
	public List<OrderDetail> findByOrderId(int orderId){
		return orderDetailRepository.findByOrderId(orderId);
	}
	
	public List<OrderDetail> findAllByProductId(int productId){
		return orderDetailRepository.findAllByProductId(productId);
	}
	
	public void updateReviewed(int orderDetailId, boolean reviewed) {
		Optional<OrderDetail> orderDetailOptional = orderDetailRepository.findById(orderDetailId);
		orderDetailOptional.ifPresent(orderDetail ->{
			orderDetail.setReviewed(reviewed);
			orderDetailRepository.save(orderDetail);
		});
	}
}
