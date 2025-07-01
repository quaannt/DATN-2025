package com.example.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Address;
import com.example.Entity.OrderAddress;
import com.example.Repository.OrderAddressRepository;

@Service
public class OrderAddressService {

	@Autowired
	OrderAddressRepository orderAddressRepository;
	
	public OrderAddress findById(int id) {
		return orderAddressRepository.findById(id).orElse(null);
	}
	
	public void save(OrderAddress orderAddress) {
		orderAddressRepository.save(orderAddress);
	}
	
	public void createOrderAddress(OrderAddress orderAddress, Address address) {
		orderAddress.setApartmentNumber(address.getApartmentNumber());
		orderAddress.setDistrict(address.getDistrict());
		orderAddress.setCity(address.getCity());
		orderAddress.setWard(address.getWard());
		orderAddress.setPhoneNumber(address.getPhoneNumber());
		save(orderAddress);
	}
}
