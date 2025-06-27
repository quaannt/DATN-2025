package com.example.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Review;
import com.example.Repository.ReviewRepository;

@Service
public class ReviewService {

	@Autowired
	ReviewRepository reviewRepository;
	
	public void save(Review review) {
		reviewRepository.save(review);
	}
	
	public List<Review> findByProductId(int productId){
		return reviewRepository.findByProductId(productId);
	}
	
	public List<Review> findAll(){
		return reviewRepository.findAll();
	}
	
	public void deleteById(int id) {
		reviewRepository.deleteById(id);
	}
}
