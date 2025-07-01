package com.example.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Cart;
import com.example.Entity.CartItem;
import com.example.Entity.Product;
import com.example.Repository.CartItemRepository;

@Service
public class CartItemService {

	@Autowired
	CartItemRepository cartItemRepository;
	

	private List<CartItem> cartItems = new ArrayList<CartItem>();
	public void addCartItem(CartItem cartItem) {
		cartItems.add(cartItem);
	}
	
	public CartItem findCartItembyProduct(Cart cart, Product product) {
		return cartItemRepository.findByCartAndProduct(cart, product);
	}
	
	public void deleteById(int id) {
		cartItemRepository.deleteById(id);;
	}
	
	public List<CartItem> findAllByProductId(int productId){
		return cartItemRepository.findAllByProductId(productId);
	}
	
	public List<CartItem> findByCart(Cart cart){
		return cartItemRepository.findByCart(cart);
	}
	
}
