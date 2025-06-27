package com.example.UserController;

import java.util.List;
import java.util.Optional;

import org.apache.el.parser.AstFalse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Entity.Address;
import com.example.Entity.Cart;
import com.example.Entity.CartItem;
import com.example.Entity.DiscountCode;
import com.example.Entity.Shipment;
import com.example.Entity.User;
import com.example.Service.AddressService;
import com.example.Service.CartService;
import com.example.Service.DiscountService;
import com.example.Service.ShipmentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class PaymentController {

	@Autowired
	CartService cartService;
	
	@Autowired
	ShipmentService shipmentService;
	
	@Autowired
	AddressService addressService;
	
	@Autowired
	DiscountService discountService;
	
	@GetMapping("/payment")
	public String showPayment(Model model, HttpServletRequest request,  @RequestParam(name = "shipmentId",required = false) Optional<Integer> shipmentId) {
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
				List<Shipment> shipments = shipmentService.findAll();
				model.addAttribute("shipments", shipments);
				List<Address> addresses = addressService.findByUserId(user.getId());
				model.addAttribute("addresses", addresses);
				
			    List<DiscountCode> discountCodes = discountService.findAll();
			    model.addAttribute("discountCodes", discountCodes);
			    return "User/payment";
			}else {
				return "redirect:/menu";
			}
			
		}
		return "User/login";
	}
	

	
}
