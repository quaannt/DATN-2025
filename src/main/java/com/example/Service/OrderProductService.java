package com.example.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Order;
import com.example.Entity.OrderProduct;
import com.example.Entity.Product;
import com.example.Repository.OrderProductRepository;

@Service
public class OrderProductService {

	@Autowired
	OrderProductRepository orderProductRepository;
	
	OrderProduct findById(int id) {
		return orderProductRepository.findById(id).orElse(null);
	}
	
	public void save(OrderProduct orderProduct) {
		orderProductRepository.save(orderProduct);
	}
	
	public List<OrderProduct> findAllByProductId(int productId){
		return orderProductRepository.findAllByProductId(productId);
	}
	
	public void createOrderProduct(OrderProduct orderProduct,Product product) {

			orderProduct.setName(product.getName());
			orderProduct.setImage(product.getImage());
			orderProduct.setPrice(product.getPromotionalPrice() != null ? product.getPromotionalPrice() : product.getPrice());
			orderProduct.setProduct(product);
			save(orderProduct);
		
	}
	
}
