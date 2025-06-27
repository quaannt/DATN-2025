package com.example.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.DiscountCode;
import com.example.Repository.DiscountRepository;

@Service
public class DiscountService {

	@Autowired
	DiscountRepository discountRepository;
	
	public List<DiscountCode> findAll(){
		return discountRepository.findAll();
	}
	
	public void save(DiscountCode discountCode) {
		discountRepository.save(discountCode);
	}
	
	public void deleteById(int id) {
		discountRepository.deleteById(id);
	}
	
	public Optional<DiscountCode> findById(int id){
		return discountRepository.findById(id);
	}
	
	public DiscountCode findByCode(String code) {
		return discountRepository.findByCode(code);
	}


}
