package com.example.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.OrderProduct;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Integer> {
	List<OrderProduct> findAllByProductId(int productId);
}
