package com.example.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Shipment;
import com.example.Repository.ShipmentRepository;

@Service
public class ShipmentService {

	@Autowired
	ShipmentRepository shipmentRepository;
	
	public List<Shipment> findAll(){
		return shipmentRepository.findAll();
	}

	
	public Shipment findById(int id){
		return shipmentRepository.findById(id).orElse(null);
	}
	
	public void save(Shipment shipment) {
		shipmentRepository.save(shipment);
	}
	
	public void delete(int id) {
		shipmentRepository.deleteById(id);
	}
}
