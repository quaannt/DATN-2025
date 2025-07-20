package com.example.Repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.DTO.ProductSalesDTO;
import com.example.Entity.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer>{

	List<OrderDetail> findByOrderId(int orderId);
	List<OrderDetail> findAllByProductId(int productId);
	
	@Query("SELECT od.product AS product, SUM(od.quantity) AS totalSold " +
		       "FROM OrderDetail od " +
		       "GROUP BY od.product " +
		       "ORDER BY totalSold DESC")
		List<ProductSalesDTO> findTopBestSellingProducts(Pageable pageable);
	

}
