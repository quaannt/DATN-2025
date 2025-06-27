package com.example.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.OrderShipment;
import com.example.Entity.Shipment;
import com.example.Repository.OrderShipmentRepository;

@Service
public class OrderShipmentService {

	@Autowired
	OrderShipmentRepository orderShipmentRepository;
	
	OrderShipment findById(int id) {
		return orderShipmentRepository.findById(id).orElse(null);
	}
	
	public void save(OrderShipment orderShipment) {
		orderShipmentRepository.save(orderShipment);
	}
	
	public void createOrderShipment(OrderShipment orderShipment, Shipment shipment) {
		orderShipment.setName(shipment.getName());
		orderShipment.setPrice(shipment.getPrice());
	}
}
