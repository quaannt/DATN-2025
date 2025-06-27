package com.example.Repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Entity.Cart;
import com.example.Entity.CartItem;
import com.example.Entity.Product;
import com.example.Entity.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer>{

	@Query("SELECT findUser FROM Cart findUser WHERE findUser.user.id = :user_id")
	Cart findByUser(@Param("user_id") int user_id);
	
	
}
