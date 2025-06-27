package com.example.DashboardController;

import java.util.List;
import java.util.Optional;

import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.DiscountCode;
import com.example.Entity.User;
import com.example.Service.DiscountService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/dashboard")
public class DiscountController {

	@Autowired
	DiscountService discountService;

	@GetMapping("/discount")
	public String showDiscount(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");

		if (user != null && (user.getRole().equals("ADMIN") || user.getRole().equals("EMPLOYEE"))) {
			List<DiscountCode> discountCodes = discountService.findAll();
			model.addAttribute("discountCodes", discountCodes);
			DiscountCode discount = new DiscountCode();
			model.addAttribute("discount", discount);
			return "Dashboard/discount";
		} else {
			return "redirect:/login";
		}

	}

	@PostMapping("/discount/save-or-update")
	public String saveOrUpdateDiscount(Model model, @ModelAttribute DiscountCode discountCode, @RequestParam("code") String code,
			RedirectAttributes redirectAttributes) {
		List<DiscountCode> discountCodes = discountService.findAll();
		model.addAttribute("discountCodes", discountCodes);
		try {
			DiscountCode existingCode = discountService.findByCode(code);
			if(existingCode != null) {
				redirectAttributes.addFlashAttribute("danger", "Mã giảm giá này đã tồn tại!");
				return "redirect:/dashboard/discount";
			}
			discountService.save(discountCode);
			redirectAttributes.addFlashAttribute("success", "Lưu mã giảm giá thành công!");
			return "redirect:/dashboard/discount";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Lưu mã giảm giá thất bại!");
			return "redirect:/dashboard/discount";
		}

	}

	@GetMapping("/discount/{id}")
	public String updateDiscount(@PathVariable int id, Model model) {
		List<DiscountCode> discountCodes = discountService.findAll();

		Optional<DiscountCode> discountGet = discountService.findById(id);
		DiscountCode discount = new DiscountCode();
		if (discountGet.isPresent()) {
			discount = discountGet.get();
		}
		model.addAttribute("discountCodes", discountCodes);
		model.addAttribute("discount", discount);
		return "Dashboard/discount";
	}

	@GetMapping("/discount/delete/{id}")
	public String deleteDiscount(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
		try {
			discountService.deleteById(id);
			redirectAttributes.addFlashAttribute("success", "xóa mã giảm giá thành công!");
			return "redirect:/dashboard/discount";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Xóa mã giảm giá thất bại!");
			return "redirect:/dashboard/discount";
		}

	}
}
