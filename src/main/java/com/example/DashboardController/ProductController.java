package com.example.DashboardController;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Config.UploadService;
import com.example.Entity.CartItem;
import com.example.Entity.Category;
import com.example.Entity.OrderProduct;
import com.example.Entity.PinnedProduct;
import com.example.Entity.Product;
import com.example.Entity.User;
import com.example.Repository.CategoryRepository;
import com.example.Service.CartItemService;
import com.example.Service.CategoryService;
import com.example.Service.OrderProductService;
import com.example.Service.PinnedProductService;
import com.example.Service.ProductService;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;

@Controller
@RequestMapping("/dashboard")
public class ProductController {

	@Autowired
	ProductService productService;

	@Autowired
	CategoryService categoryService;
	
	@Autowired
	PinnedProductService pinnedProductService;

	@Autowired
	UploadService uploadService;
	
	@Autowired
	OrderProductService orderProductService;
	
	@Autowired
	CartItemService cartItemService;
	
    @Value("${upload.dir}")
    private String uploadDir;

    @Value("${static-folder}")
    private String staticFolder;
	
	@GetMapping("/product")
	public String product(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");

		if (user != null && (user.getRole().equals("ADMIN") || user.getRole().equals("EMPLOYEE"))) {
			List<Product> products = productService.findAll();
			model.addAttribute("products", products);
			Product product = new Product();
			model.addAttribute("product", product);
			List<Category> categories = categoryService.findAll();
			model.addAttribute("categories", categories);
			
			List<PinnedProduct> pinnedProducts = pinnedProductService.findAll();
			Set<Integer> pinnedProductIds = pinnedProducts.stream()
			    .map(p -> p.getProduct().getId())
			    .collect(Collectors.toSet());

			model.addAttribute("pinnedProductIds", pinnedProductIds);
			return "Dashboard/product";
		} else {
			return "redirect:/login";
		}
	}

	@PostMapping("/product/save-or-update")
	public String saveOrUpdate(@ModelAttribute Product product, Model model, RedirectAttributes redirectAttributes,
			@RequestParam("file") MultipartFile file) {

		try {
			Product exisingProduct = productService.findById(product.getId()).orElse(null);
			if(file != null && !file.isEmpty()) {
				 String filePhoto = uploadService.saveFile(file, "images");
				 product.setImage(staticFolder + "images/" + filePhoto);
			}else {
				
				product.setImage(exisingProduct.getImage());
			}
			
			System.out.println("Status: " + product.isStatus());
			
			productService.save(product);
			redirectAttributes.addFlashAttribute("success", "Lưu sản phẩm thành công!");
			return "redirect:/dashboard/product";
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("danger", "Vui lòng kiểm tra lại thông tin!");
			return "redirect:/dashboard/product";
		}
	}

	@GetMapping("/product/{id}")
	public String showUpdate(@PathVariable int id, Model model) {
		List<Product> products = productService.findAll();
		model.addAttribute("products", products);
		List<Category> categories = categoryService.findAll();
		model.addAttribute("categories", categories);

		Optional<Product> productget = productService.findById(id);
		Product product = new Product();
		if (productget.isPresent()) {
			product = productget.get();
		}
		model.addAttribute("status", product.isStatus());
		model.addAttribute("product", product);
		

		List<PinnedProduct> pinnedProducts = pinnedProductService.findAll();
		Set<Integer> pinnedProductIds = pinnedProducts.stream()
		    .map(p -> p.getProduct().getId())
		    .collect(Collectors.toSet());

		model.addAttribute("pinnedProductIds", pinnedProductIds);
		
		return "Dashboard/product";
	}

	@GetMapping("/product/delete/{id}")
	public String deleteProduct(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
		try {
			List<OrderProduct> orderProducts = orderProductService.findAllByProductId(id);
			for(OrderProduct orderProduct : orderProducts) {
				orderProduct.setProduct(null);
				orderProductService.save(orderProduct);
			}
			List<CartItem> cartItems = cartItemService.findAllByProductId(id);
			for(CartItem cartItem : cartItems) {
				cartItemService.deleteById(cartItem.getId());
			}
			Product product = productService.findById(id).orElse(null);
			product.getCartItems().clear(); 
			productService.deleteById(id);
			redirectAttributes.addFlashAttribute("success", "Xóa sản phẩm thành công!");
			return "redirect:/dashboard/product";
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("danger", "Xóa sản phẩm thất bại!");
			return "redirect:/dashboard/product";
		}
	}

	@GetMapping("/product/pin/{id}")
	public String pinnedProduct(HttpSession httpSession, @PathVariable int id, Model model,
			RedirectAttributes redirectAttributes) {
		Product product = productService.findById(id).get();
		PinnedProduct pinnedProduct = new PinnedProduct();
		pinnedProduct.setType("HOT");
		pinnedProduct.setProduct(product);
		try {
			pinnedProductService.save(pinnedProduct);
			redirectAttributes.addFlashAttribute("success", "Ghim sản phẩm lên trang chính thành công!");
			
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Sảm phẩm này đã được ghim trước đó!");
			
		}
		return "redirect:/dashboard/product";
	}
	
	@GetMapping("/product/un-pin/{id}")
	public String unpinProduct(HttpSession httpSession, @PathVariable int id, Model model,
			RedirectAttributes redirectAttributes) { 
		try {
			pinnedProductService.deleteByProductId(id);
			redirectAttributes.addFlashAttribute("success", "Bỏ ghim sản phẩm thành công!");
			
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Bỏ ghim sản phẩm thất bại!");
			
		}
		return "redirect:/dashboard/product";
	}
}
