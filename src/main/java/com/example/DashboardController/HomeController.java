package com.example.DashboardController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Entity.Order;
import com.example.Entity.Product;
import com.example.Entity.User;
import com.example.Service.OrderService;
import com.example.Service.ProductService;
import com.example.Service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller(value = "homeDashboardController")
@RequestMapping("/dashboard")
public class HomeController {
	
	@Autowired
	ProductService productService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	UserService userService;

	@GetMapping("/index")
	public String indexDashboard(HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		
		if(user != null && (user.getRole().equals("ADMIN")|| user.getRole().equals("EMPLOYEE"))) {
			List<Product> products = productService.findAll();
			int numberOfProducts = products.size();
			model.addAttribute("numberOfProducts", numberOfProducts);
			List<Order> orders = orderService.findAll();
			int numberOfOrders = orders.size();
			model.addAttribute("numberOfOrders", numberOfOrders);
			List<User> users = userService.findAll();
			int numberOfUsers = users.size();
			model.addAttribute("numberOfUsers", numberOfUsers);
			
			return "Dashboard/index";
		}else {
			return "redirect:/login";
		}	
	}
	
	
}
