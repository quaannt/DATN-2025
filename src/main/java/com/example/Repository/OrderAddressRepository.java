package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.OrderAddress;

@Repository
public interface OrderAddressRepository extends JpaRepository<OrderAddress, Integer> {
}
