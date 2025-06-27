package com.example.DashboardController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.Review;
import com.example.Entity.User;
import com.example.Service.ReviewService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/dashboard")
public class ReviewMangementController {

	@Autowired
	ReviewService reviewService;

	@GetMapping("/review")
	public String showReview(HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		if (user != null && (user.getRole().equals("ADMIN") || user.getRole().equals("EMPLOYEE"))) {
			List<Review> reviews = reviewService.findAll();
			model.addAttribute("reviews", reviews);
			return "Dashboard/review";
		} else {
			return "redirect:/login";
		}
	}
	
	@GetMapping("/productDetail/{productId}")
	public String toProduct(@PathVariable("productId") int productId) {
		return "redirect:/productDetail/" + productId;
	}
	
	@GetMapping("/review/delete/{id}")
	public String deleteReview(@PathVariable int id, RedirectAttributes redirectAttributes) {
		try {
			reviewService.deleteById(id);
			redirectAttributes.addFlashAttribute("success", "Xóa đánh giá thành công!");
			return "redirect:/dashboard/review";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Xóa đánh giá thất bại!");
			return "redirect:/dashboard/review";
		}
	}

}
