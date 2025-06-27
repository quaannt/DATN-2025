package com.example.UserController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Entity.Category;
import com.example.Entity.Product;
import com.example.Entity.Review;
import com.example.Entity.User;
import com.example.Repository.ProductRepository;
import com.example.Service.CartService;
import com.example.Service.CategoryService;
import com.example.Service.ProductService;
import com.example.Service.ReviewService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserProductController {

	@Autowired
	ProductService productService;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	CartService cartService;
	
	@Autowired
	ReviewService reviewService;
	
	@Autowired
	ProductRepository productRepository;
	
	@GetMapping("/menu")
	public String Viewproduct(Model model, HttpServletRequest request ) {
		HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user != null) {
        	int cartId = user.getCart().getId();
    		Double cartTotal = cartService.calculateTotalForCart(cartId);
    		model.addAttribute("cartTotal", cartTotal);
        }
        
		List<Product> products = productService.findAll();
		model.addAttribute("products", products);
		List<Category> categories = categoryService.findAll();
		model.addAttribute("categories", categories);
		return "User/menu";
	}
	
	@GetMapping( "/productDetail/{id}")
	public String ProductDetail(Model model, @PathVariable Integer id) {
		List<Product> products = productService.findAll();
		model.addAttribute("products", products);
		Product product = productService.findone(id);
		model.addAttribute("product", product);
		List<Review> reviews = reviewService.findByProductId(id);
		
		Double basePrice = (product.getPromotionalPrice() != null) ? product.getPromotionalPrice() : product.getPrice();
		List<Product> similarPriceProducts = productRepository.findTop5ByClosestPrice(product.getId(), basePrice);
		model.addAttribute("similarPriceProducts", similarPriceProducts);
		
		model.addAttribute("reviews", reviews);
		int numberOfReviews = reviews.size();
		model.addAttribute("numberOfReviews", numberOfReviews);
		return "User/productDetail";
	}
	
	
}
