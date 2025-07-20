package com.example.Repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.DTO.ProductReviewCountDTO;
import com.example.Entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>{

	List<Review> findByProductId(int productId);
	
	@Query("SELECT r.product AS product, COUNT(r) AS reviewCount " +
		       "FROM Review r " +
		       "GROUP BY r.product " +
		       "ORDER BY reviewCount DESC")
		List<ProductReviewCountDTO> findTopReviewedProducts(Pageable pageable);
}
