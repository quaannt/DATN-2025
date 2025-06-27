package com.example.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Category;
import com.example.Repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	CategoryRepository categoryrRepository;
	
	public List<Category> findAll(){
		return categoryrRepository.findAll();
	}
	
	public Optional<Category> findById(int id) {
		return categoryrRepository.findById(id);
	}
	
	public void save(Category category) {
		categoryrRepository.save(category);
	}
	
	public void deleteById(int id) {
		categoryrRepository.deleteById(id);
	}
	
	
}
