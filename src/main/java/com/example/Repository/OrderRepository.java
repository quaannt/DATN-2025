package com.example.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{
	
	@Query("SELECT findUser FROM Order findUser WHERE findUser.user.id = :user_id")
	Order findByUser(@Param("user_id") int user_id);
	List<Order> findByUserId(int userId);
	
	@Query("SELECT SUM(o.totalPaymentAmount) " +
		       "FROM Order o " +
		       "WHERE o.orderStatus IN :statuses")
		Double getTotalPaymentAllTime(@Param("statuses") List<String> statuses);
	
	@Query("SELECT SUM(o.totalPaymentAmount) " +
		       "FROM Order o " +
		       "WHERE FUNCTION('MONTH', o.orderDate) = :month " +
		       "AND FUNCTION('YEAR', o.orderDate) = :year " +
		       "AND o.orderStatus IN :statuses")
		Double getTotalPaymentInMonth(@Param("month") int month,
		                               @Param("year") int year,
		                               @Param("statuses") List<String> statuses);
}
