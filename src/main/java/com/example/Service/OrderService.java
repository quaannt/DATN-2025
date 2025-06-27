package com.example.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Cart;
import com.example.Entity.CartItem;
import com.example.Entity.Order;
import com.example.Entity.OrderDetail;
import com.example.Entity.Product;
import com.example.Entity.User;
import com.example.Repository.OrderDetailRepository;
import com.example.Repository.OrderRepository;

@Service
public class OrderService {

	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	OrderDetailRepository orderDetailRepository;
	
	@Autowired
	DiscountService discountService;
	
	public Order getOrCreateOrder(User user) {
		Order existingOrder = orderRepository.findByUser(user.getId());
		if(existingOrder == null) {
			Order newOrder = new Order();
			newOrder.setUser(user);
			return newOrder;
		}
		return existingOrder;
	}
	

	public Optional<Order> findById(int id){
		return orderRepository.findById(id);
	}
	
	public void updateStatus(int orderId, String orderStatus, String paymentStatus) {
		Optional<Order> optionalOrder = orderRepository.findById(orderId);
		if(optionalOrder.isPresent()) {
			Order order = optionalOrder.get();
			order.setOrderStatus(orderStatus);
			order.setPaymentStatus(paymentStatus);
			orderRepository.save(order);
		}else {
			throw new RuntimeException("Không tìm thấy đơn hàng với ID:" + orderId);
		}
	}
	
	public void updateOrderStatus(int orderId, String status) {
		Optional<Order> orderOptional = orderRepository.findById(orderId);
		if(orderOptional.isPresent()) {
			Order order = orderOptional.get();
			order.setOrderStatus(status);
			orderRepository.save(order);
		}else {
			throw new RuntimeException("Không tìm thấy đơn hàng với ID:" + orderId);
		}
	}
	
	

	
	public void save(Order order) {
		orderRepository.save(order);
	}
	
	public List<Order> findAll(){
		return orderRepository.findAll();
	}
	
	public void deleteById(int id) {
		orderRepository.deleteById(id);
	}
	
	public List<Order> findByUserId(int userId){
		return orderRepository.findByUserId(userId);
	}
}
