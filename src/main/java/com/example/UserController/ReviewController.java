package com.example.UserController;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.OrderDetail;
import com.example.Entity.Product;
import com.example.Entity.Review;
import com.example.Service.OrderDetailService;
import com.example.Service.ProductService;
import com.example.Service.ReviewService;

@Controller
public class ReviewController {
	
	@Autowired
	ReviewService reviewService;
	
	@Autowired
	OrderDetailService orderDetailService;

	@Autowired
	ProductService productService;
	
	@PostMapping("review/create")
	public String createReview(@RequestParam("orderDetailId")int orderDetailId, @RequestParam("rating") int rating, @RequestParam("comment") String conmment, RedirectAttributes redirectAttributes) {
		
		try {
			
			OrderDetail orderDetail = orderDetailService.findById(orderDetailId);
			
			if (orderDetail == null) {
	            // Xử lý khi không tìm thấy OrderDetail
	            redirectAttributes.addFlashAttribute("danger", "Không tìm thấy chi tiết đơn hàng!");
	            return "redirect:/order";
	        }
			
			Product product = productService.findById(orderDetail.getOrderProduct().getProduct().getId()).orElse(null);
			
			Review review = new Review();
			 review.setRating(rating);
			 review.setComment(conmment);
			 review.setDate(new Date());
			 review.setOrderDetail(orderDetail);
			 review.setProduct(product);
			 reviewService.save(review);
			 orderDetailService.updateReviewed(orderDetailId, true);
			 redirectAttributes.addFlashAttribute("success", "Đánh giá thành công!");
			 return "redirect:/order";
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Đánh giá thất bại!");
			return "redirect:/order";
		}
	}
}
