package com.example.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	@Query("SELECT o FROM User o WHERE o.email = :e")
	public com.example.Entity.User findByEmail(@Param(value = "e") String Email);
	
	 boolean existsByEmail(String email);
	 User findByToken(String token);

}