package com.example.Controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Entity.Category;
import com.example.Entity.Order;
import com.example.Entity.PinnedProduct;
import com.example.Entity.Product;
import com.example.Entity.User;
import com.example.Service.CartService;
import com.example.Service.CategoryService;
import com.example.Service.OrderService;
import com.example.Service.PinnedProductService;
import com.example.Service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	CartService cartService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	ProductService productService;
	
	@Autowired 
	CategoryService categoryService;
	
	@Autowired
	PinnedProductService pinnedProductService;
	
	@GetMapping({"/index", "", "/"})
	public String index(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user != null) {
        	cartService.getOrCreateCart(user);
        	int cartId = user.getCart().getId();
    		Double cartTotal = cartService.calculateTotalForCart(cartId);
    		model.addAttribute("cartTotal", cartTotal);
    		List<Order> orders = orderService.findByUserId(user.getId());
    		model.addAttribute("orders", orders);
    		
    		
        }
        List<Product> products = productService.findAll();
		model.addAttribute("products", products);
		List<PinnedProduct> pinnedProducts = pinnedProductService.findAll();
		model.addAttribute("pinnedProducts", pinnedProducts);
		return "User/index";
	}
	
	@GetMapping("/products")
	public String find(Model model, @RequestParam(value = "search", required = false, defaultValue = "") String search) {
	    List<Product> products = productService.findAll();
	    products.sort(Comparator.comparing(Product::getId).reversed());
	    
	    List<Category> categories = categoryService.findAll();
	    model.addAttribute("categories", categories);

	    if (!search.isBlank()) {
	        String[] keywords = search.toLowerCase().split("\\s+");

	        products = products.stream().filter(product -> {
	            String name = product.getName().toLowerCase();

	            return Arrays.stream(keywords).allMatch(keyword -> {
	                if (name.contains(keyword)) return true;

	                return Arrays.stream(name.split("\\s+"))
	                        .anyMatch(word -> levenshtein(word, keyword) <= 2);
	            });
	        }).toList();
	    } else {
	        products = Collections.emptyList();
	    }
	    
	    model.addAttribute("products", products);
	    return "User/menu";
	}
	
	@GetMapping("/filter")
	public String filterByCategory(@RequestParam("categoryId") int categoryId, Model model) {
		List<Category> categories = categoryService.findAll();
		model.addAttribute("categories", categories);
		List<Product> filteredProducts = productService.findByCategoryId(categoryId);
		filteredProducts.sort(Comparator.comparing(Product::getId).reversed());
		model.addAttribute("products", filteredProducts);
		return "User/menu";
	}
	
	@GetMapping(value = "/about-us")
	public String about() {
		return "User/about-us";
	}

	public static int levenshtein(String a, String b) {
	    int[][] dp = new int[a.length() + 1][b.length() + 1];

	    for (int i = 0; i <= a.length(); i++) {
	        for (int j = 0; j <= b.length(); j++) {
	            if (i == 0) {
	                dp[i][j] = j;
	            } else if (j == 0) {
	                dp[i][j] = i;
	            } else if (a.charAt(i - 1) == b.charAt(j - 1)) {
	                dp[i][j] = dp[i - 1][j - 1];
	            } else {
	                dp[i][j] = 1 + Math.min(dp[i - 1][j - 1],
	                               Math.min(dp[i - 1][j], dp[i][j - 1]));
	            }
	        }
	    }

	    return dp[a.length()][b.length()];

}

	
}