package com.example.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Cart;
import com.example.Entity.CartItem;
import com.example.Entity.CartItemInfo;
import com.example.Entity.Product;
import com.example.Entity.User;
import com.example.Repository.CartItemRepository;
import com.example.Repository.CartRepository;
import com.example.Repository.ProductRepository;
import com.example.Repository.UserRepository;

@Service
public class CartService {


	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	CartItemRepository cartItemRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	public Cart getOrCreateCart(User user) {		
		Cart existingCart = cartRepository.findByUser(user.getId());		
		if(existingCart == null) {
			Cart newCart= new Cart();
			newCart.setUser(user);
			newCart.setTotalAmount(0.0);
			cartRepository.save(newCart);
			return newCart;
		}		
		return existingCart;
	}
	
	public void addToCart(Cart cart, Product product, int quantity) {
		CartItem cartItem = new CartItem();
		cartItem.setCart(cart);
		cartItem.setProduct(product);
		cartItem.setQuantity(quantity);
		cartItem.setPrice(product.getPrice());
		if(product.getPromotionalPrice() != null) {
			cartItem.setTotalAmount(quantity * product.getPromotionalPrice());
		}else {
			cartItem.setTotalAmount(quantity * product.getPrice());
		}
		cartItemRepository.save(cartItem);
	}
	
	public List<CartItem> getCartItemGroup(int CartId){
		List<CartItemInfo> cartItemInfo = cartItemRepository.getCartItemGroup(CartId); 
		List<CartItem> cartItems = new ArrayList<CartItem>();
		for(CartItemInfo c:cartItemInfo) {
			CartItem cartitem = new CartItem();
			cartitem.setCart(cartRepository.findById(CartId).get());
			cartitem.setProduct(productRepository.findById(c.getProductId()).get());
			
			cartitem.setQuantity((int)c.getQuantity());
			cartitem.setTotalAmount((double)c.getTotalAmount());

			cartItems.add(cartitem);
			
		}
		
		return cartItems;
		
	}
	
	
	public void removeProductFromCart(int productId, Cart cart) {
		cartItemRepository.deleteByProductIdAndCartId(productId, cart.getId());
	}
	 
	public void clearCart(int cartId) {
		Optional<Cart> optionalCart = cartRepository.findById(cartId);
		if(optionalCart.isPresent()) {
			Cart cart = optionalCart.get();
			cartItemRepository.deleteByCart(cart);
		}
	}
	
	public void deleteItem(int productId, Cart cart) {
		List<CartItem> listProduct = cartItemRepository.findByProductIdAndCartId(productId, cart.getId());
		cartItemRepository.deleteById(listProduct.get(0).getId());
	}
	
	public double calculateTotalForCart(int cartId) {
		Optional<Cart> optionalCart = cartRepository.findById(cartId);
		if(optionalCart.isPresent()) {
			Cart cart = optionalCart.get();
			return cart.caculateTotal();
		}
		return 0.0;
	}
	
	public Optional<Cart> findById(int id) {
		return cartRepository.findById(id);
	}
	
	public void deleteCart(Cart cart) {
		cartRepository.delete(cart);
	}
}
