package com.example.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.Category;
import com.example.Entity.Product;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{
	
}
