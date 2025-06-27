package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.OrderDiscount;

@Repository
public interface OrderDiscountRepository extends JpaRepository<OrderDiscount, Integer> {

}
