package com.example.ControllerAdvice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.Entity.Cart;
import com.example.Entity.CartItem;
import com.example.Entity.User;
import com.example.Service.CartItemService;
import com.example.Service.CartService;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class CartControllerAdvice {

	@Autowired
	CartService cartService;
	
	@Autowired
	CartItemService cartItemService;
	
	@ModelAttribute
	public void getUserCart(HttpSession session, Model model) {
		  User user = (User) session.getAttribute("user");
		  if(user!= null) {
			  Cart cart = cartService.findById(user.getCart().getId()).orElse(null);
			  model.addAttribute("cart", cart);
			  List<CartItem> cartItems = cartItemService.findByCart(cart);
			  model.addAttribute("cartItemsAdvice", cartItems);
		  }
		
	}
}
