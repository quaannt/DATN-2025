package com.example.Controller;

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
		List<Category> categories = categoryService.findAll();
		model.addAttribute("categories", categories);
		if(search.length() > 0) {
			products = products.stream().filter(pd -> pd.getName().contains(search)).toList();
		}
		model.addAttribute("products", products);
		return "User/menu";
	}
	
	@GetMapping("/filter")
	public String filterByCategory(@RequestParam("categoryId") int categoryId, Model model) {
		List<Category> categories = categoryService.findAll();
		model.addAttribute("categories", categories);
		List<Product> filteredProducts = productService.findByCategoryId(categoryId);
		model.addAttribute("products", filteredProducts);
		return "User/menu";
	}
	
	@GetMapping(value = "/about")
	public String about() {
		return "User/about";
	}

}
