package com.example.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Entity.PinnedProduct;

@Repository
public interface PinnedProductRepository extends JpaRepository<PinnedProduct, Integer>{

	@Query("SELECT o FROM PinnedProduct o WHERE o.product.id =:pId")
    public PinnedProduct findByProductId(@Param("pId") int pId);
	
}
