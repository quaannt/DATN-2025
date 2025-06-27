package com.example.UserController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Entity.Cart;
import com.example.Entity.CartItem;
import com.example.Entity.Product;
import com.example.Entity.User;
import com.example.Repository.CartItemRepository;
import com.example.Service.CartItemService;
import com.example.Service.CartService;
import com.example.Service.ProductService;
import com.example.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;



@Controller
public class CartController {

	@Autowired
	UserService userService;
	
	@Autowired 
	CartService cartService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	CartItemService cartItemService;
	
	@GetMapping(value = "/cart")
	public String cart(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user != null) {
        	Cart cart = cartService.getOrCreateCart(user);
        	if(cart != null && !cart.getCartItems().isEmpty()) {
        		List<CartItem> cartItems = cartService.getCartItemGroup(cart.getId());
        		int cartId = user.getCart().getId();
    			Double cartTotal = cartService.calculateTotalForCart(cartId);
    			model.addAttribute("cartTotal", cartTotal);
        		model.addAttribute("cartItems", cartItems) ;       	
        		}
        	return "User/cart";
        }else {
        	return "redirect:/login";
		}
       
	}
	
	@GetMapping("/cart/add-to-cart")
	public String addToCart(@RequestParam("productId") int id,HttpSession session) {

		User user = (User) session.getAttribute("user");
		Optional<Product> product = productService.findById(id);
		
		if(user != null && product.isPresent()) {
			Cart cart = cartService.getOrCreateCart(user);
			cartService.addToCart(cart, product.get(),1);
		}
		return "redirect:/cart";
	}
	
	@GetMapping("/cart/removeProduct/{productId}")
	public String removeProductFromCart(@PathVariable int productId,HttpSession session) {
		User user = (User) session.getAttribute("user");
		Cart cart = cartService.getOrCreateCart(user);
		
		cartService.removeProductFromCart(productId, cart);
		return "redirect:/cart";
	}
	
	@GetMapping("/cart/clearCart")
	public String clearCart(HttpSession session){
		User user = (User) session.getAttribute("user");
		if(user != null && user.getCart() != null) {
			int cartId = user.getCart().getId();
			cartService.clearCart(cartId);
		}
		
		return "redirect:/cart";
	}
	
	@GetMapping("/cart/deleteItem")
	public String deleteProd(@RequestParam("productId") int productId, HttpSession session) {
		User user = (User) session.getAttribute("user");
		
		if(user != null && user.getCart() != null) {
			
			cartService.deleteItem(productId, user.getCart());
			
		}
		
		return "redirect:/cart";
	}
	


}
