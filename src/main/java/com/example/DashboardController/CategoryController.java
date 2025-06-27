package com.example.DashboardController;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.Category;
import com.example.Entity.User;
import com.example.Service.CategoryService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/dashboard")
public class CategoryController {

	@Autowired
	CategoryService categoryService;

	@GetMapping("/category")
	public String showCategory(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");

		if (user != null && (user.getRole().equals("ADMIN") || user.getRole().equals("EMPLOYEE"))) {
			List<Category> categories = categoryService.findAll();
			model.addAttribute("categories", categories);
			Category category = new Category();
			model.addAttribute("category", category);
			return "Dashboard/category";
		} else {
			return "redirect:/login";
		}

	}

	@PostMapping("/category/save-or-update")
	public String saveCategory(Model model, @ModelAttribute Category category, RedirectAttributes redirectAttributes) {
		List<Category> categories = categoryService.findAll();
		model.addAttribute("categories", categories);
		try {
			categoryService.save(category);
			redirectAttributes.addFlashAttribute("success", "Lưu danh mục thành công!");
			return "redirect:/dashboard/category";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Lưu danh mục thất bại!");
			return "redirect:/dashboard/category";
		}
	}

	@GetMapping("category/{id}")
	public String updateCategory(Model model, @PathVariable int id) {
		List<Category> categories = categoryService.findAll();
		model.addAttribute("categories", categories);

		Optional<Category> categoryGet = categoryService.findById(id);
		Category category = new Category();
		if (categoryGet.isPresent()) {
			category = categoryGet.get();
		}
		model.addAttribute("category", category);
		return "Dashboard/category";
	}

	@GetMapping("category/delete/{id}")
	public String deleteCategory(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
		try {
			categoryService.deleteById(id);
			redirectAttributes.addFlashAttribute("success", "Xoá danh mục thành công!");
			return "redirect:/dashboard/category";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Xóa danh mục thất bại!");
			return "redirect:/dashboard/category";
		}
	}
}
