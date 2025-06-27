package com.example.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Address;
import com.example.Repository.AddressRepository;

@Service
public class AddressService {
	
	@Autowired
	AddressRepository addressRepository;
	
	public List<Address> findByUserId(int userId){
		return addressRepository.findByUserId(userId);
	}

	public void save(Address address) {
		addressRepository.save(address);
	}
	
	public void delete(int id) {
		addressRepository.deleteById(id);
	}
	
	public Optional<Address> findById(int addressId){
		return addressRepository.findById(addressId);
	}
	
	public List<Address> findAll(){
		return addressRepository.findAll();
	}
}
