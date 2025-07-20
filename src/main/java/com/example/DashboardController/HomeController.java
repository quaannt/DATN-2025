package com.example.DashboardController;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.DTO.ProductReviewCountDTO;
import com.example.DTO.ProductSalesDTO;
import com.example.Entity.DiscountCode;
import com.example.Entity.Order;
import com.example.Entity.Product;
import com.example.Entity.User;
import com.example.Repository.OrderDetailRepository;
import com.example.Repository.OrderRepository;
import com.example.Repository.ReviewRepository;
import com.example.Service.DiscountService;
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

	@Autowired
	DiscountService discountService;
	
	@Autowired
	OrderDetailRepository orderDetailRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	ReviewRepository reviewRepository;
	
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
			List<DiscountCode> DiscountCodes = discountService.findAll();
			model.addAttribute("numberOfDiscountCode", DiscountCodes.size());
			
			List<ProductSalesDTO> top5Products = orderDetailRepository.findTopBestSellingProducts(PageRequest.of(0, 5));
			model.addAttribute("top5Products", top5Products);
			
			int currentMonth = LocalDate.now().getMonthValue();
			int currentYear = LocalDate.now().getYear();

			List<String> statuses = Arrays.asList("APPLYING", "SHIPPING", "PAYING", "RECEIVED");

			Double totalAllTime = orderRepository.getTotalPaymentAllTime(statuses);
			Double totalInMonth = orderRepository.getTotalPaymentInMonth(currentMonth, currentYear, statuses);

			totalAllTime = totalAllTime != null ? totalAllTime : 0.0;
			totalInMonth = totalInMonth != null ? totalInMonth : 0.0;

			model.addAttribute("totalPaymentAllTime", totalAllTime);
			model.addAttribute("totalPaymentInMonth", totalInMonth);
			
			model.addAttribute("currentMonth", currentMonth);
			
			List<ProductReviewCountDTO> topReviewedProducts = reviewRepository.findTopReviewedProducts(PageRequest.of(0, 5));
			model.addAttribute("topReviewedProducts", topReviewedProducts);
			
			return "Dashboard/index";
		}else {
			return "redirect:/login";
		}	
	}
	
	
}
