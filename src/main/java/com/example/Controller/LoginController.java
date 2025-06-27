package com.example.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Entity.User;
import com.example.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
class Res{
	boolean hasError;
	String message;
}

@Controller
public class LoginController {

	
	
	@Autowired
	UserService userService;
	
	@GetMapping(value = "/login")
	public String showLogin() {
		return "User/login";
	}
	
	@PostMapping("/login")
	public String login(Model model,HttpServletRequest request,@RequestParam(name = "email")String email, @RequestParam(name = "password")String password) {
		User userLogin = userService.findByEmail(email);
		if(userLogin != null) {
			if(userLogin.getPassword().equals(password)) {
				if (userLogin.isEnabled()) { 	
				HttpSession session = request.getSession();
				session.setAttribute("user", userLogin);
				if(userLogin.getRole().equals("ADMIN") || userLogin.getRole().equals("EMPLOYEE")) {
					return "redirect:/dashboard/index";
				}else {
					return "redirect:/index";
				}
				}else {
					model.addAttribute("res",new Res(true, "Tài khoản chưa được kích hoạt vui lòng liên hệ với nhà hàng!"));
					return "User/login";
				}
			}
		}
		model.addAttribute("res",new Res(true, "Kiểm tra lại thông tin đăng nhập!"));
		return "User/login";
	}
	
	@GetMapping("logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("user");
		return "redirect:/index";
	}
}
