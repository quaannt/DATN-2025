package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.OrderShipment;

@Repository
public interface OrderShipmentRepository extends JpaRepository<OrderShipment, Integer>{

}
