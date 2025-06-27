package com.example.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.User;
import com.example.Repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	
	public List<User> findAll(){
		return userRepository.findAll();
	}
	
	public Optional<User> findById(int id) {
		return userRepository.findById(id);
	}
	
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public void save(User user) {
		userRepository.save(user);
	}
	
	public void deleteById(int id) {
		userRepository.deleteById(id);
	}
	
	 public boolean existsByEmail(String email) {
	        return userRepository.existsByEmail(email);
	    }
	 
	 public User findByToken(String token) {
		 return userRepository.findByToken(token);
	 }

}
