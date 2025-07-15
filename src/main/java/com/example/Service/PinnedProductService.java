package com.example.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.PinnedProduct;
import com.example.Repository.PinnedProductRepository;

@Service
public class PinnedProductService {

	@Autowired
	PinnedProductRepository pinnedProductRepository; 
	
	public PinnedProduct findByProductId(int productId){
		return  pinnedProductRepository.findByProductId(productId);
	}
	
	
	public void save(PinnedProduct pinnedProduct) {
		pinnedProductRepository.save(pinnedProduct);
	}
	
	public void deleteById(int id) {
		pinnedProductRepository.deleteById(id);
	}
	
	public void deleteByProductId(int productId) {
		pinnedProductRepository.delete(pinnedProductRepository.findByProductId(productId));
	}
	
	public List<PinnedProduct> findAll() {
		return pinnedProductRepository.findAll();
	}
	

}
