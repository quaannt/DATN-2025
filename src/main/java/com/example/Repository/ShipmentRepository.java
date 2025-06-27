package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.Shipment;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Integer>{

}
