package com.example.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
	Product findOneById(Integer id);
	List<Product> findByCategoryId(int categoryId);
	
	@Query("SELECT p FROM Product p WHERE p.id NOT IN (SELECT pp.product.id FROM PinnedProduct pp)")
	List<Product> findUnpinnedProducts();
	
	@Query(value = """
			  SELECT * FROM products 
			  WHERE id <> :productId 
			    AND COALESCE(promotional_price, price) IS NOT NULL
			  ORDER BY ABS(COALESCE(promotional_price, price) - :basePrice)
			  LIMIT 5
			""", nativeQuery = true)
			List<Product> findTop5ByClosestPrice(@Param("productId") int productId, @Param("basePrice") double basePrice);
}
