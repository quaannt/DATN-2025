package com.example.DashboardController;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.Category;
import com.example.Entity.Product;
import com.example.Entity.User;
import com.example.Service.CartService;
import com.example.Service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/dashboard")
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	CartService cartService;

	@GetMapping("/user")
	public String showUser(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");

		if (user != null && (user.getRole().equals("ADMIN") || user.getRole().equals("EMPLOYEE"))) {
			List<User> users = userService.findAll();
			model.addAttribute("users", users);
			User userE = new User();
			model.addAttribute("user", userE);
			return "Dashboard/user";
		} else {
			return "redirect:/login";
		}

	}

	@PostMapping("/user/save-or-update")
	public String saveOrupdateUser(Model model, @ModelAttribute User user, RedirectAttributes redirectAttributes,
			@RequestParam("file") MultipartFile file, @RequestParam("email") String email) {

		User existingEmail = userService.findByEmail(email);
		if (existingEmail != null && existingEmail.getId() != user.getId()) {
			redirectAttributes.addFlashAttribute("danger", "Lỗi trùng lặp email!");
			return "redirect:/dashboard/user";
		}
		try {
			if (!file.isEmpty()) {
				String uploadDir = "src/main/resources/static/Dashboard/img/userImg";
				Path copyLocation = Paths.get(uploadDir + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
				user.setImage("/Dashboard/img/userImg/" + file.getOriginalFilename());
			} else {
				User existingUser = userService.findById(user.getId()).orElse(new User());
				user.setImage(existingUser.getImage());
			}

			userService.save(user);
			cartService.getOrCreateCart(user);
			redirectAttributes.addFlashAttribute("success", "Lưu người dùng thành công!");
			return "redirect:/dashboard/user";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Lưu người dùng thất bại!");
			return "redirect:/dashboard/user";
		}
	}

	@GetMapping("/user/{id}")
	public String updateUser(@PathVariable int id, Model model) {
		List<User> users = userService.findAll();
		model.addAttribute("users", users);
		Optional<User> userGet = userService.findById(id);
		User user = new User();
		if (userGet.isPresent()) {
			user = userGet.get();
		}
		model.addAttribute("user", user);
		return "Dashboard/user";
	}

	@GetMapping("/user/delete/{id}")
	public String deleteUser(@PathVariable int id, RedirectAttributes redirectAttributes, HttpSession session) {
		try {
			Optional<User> userToDelete = userService.findById(id);
			User user = (User) session.getAttribute("user");
			if (userToDelete.isPresent() && userToDelete.get().getRole().equals("ADMIN")) {
				redirectAttributes.addFlashAttribute("danger", "Không thể xóa tài khoản ADMIN!");
				return "redirect:/dashboard/user";
			}
			if(user != null && userToDelete.isPresent() && user.getId() == userToDelete.get().getId()) {
				redirectAttributes.addFlashAttribute("danger", "Không thể xóa tài khoản đang đăng nhập!");
				return "redirect:/dashboard/user";
			}
			userService.deleteById(id);
			redirectAttributes.addFlashAttribute("success", "Xóa người dùng thành công!");
			return "redirect:/dashboard/user";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Xóa người dùng thất bại!");
			return "redirect:/dashboard/user";
		}

	}
}
