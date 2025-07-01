package com.example.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.OrderProduct;
import com.example.Entity.Product;
import com.example.Repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	ProductRepository productRepository;
	
	public List<Product> findAll() {
		return productRepository.findAll();
	}
	
	public Optional<Product> findById(int id) {
		return productRepository.findById(id);
	}
	
	public void save(Product product) {
		productRepository.save(product);
	}
	
	public void deleteById(int id) {

		productRepository.deleteById(id);
	}
	
	public Product findone(Integer id) {
		return productRepository.findOneById(id);
	}
	
	public List<Product> findByCategoryId(int categoryId) {
		return productRepository.findByCategoryId(categoryId);
	}
	
	public List<Product> findUnpinnedProducts() {
	    return productRepository.findUnpinnedProducts();
	}
}
