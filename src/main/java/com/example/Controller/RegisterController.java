package com.example.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Config.TokenGenerator;
import com.example.Entity.User;
import com.example.Service.CartService;
import com.example.Service.EmailService;
import com.example.Service.UserService;

@Controller
public class RegisterController {
	
	@Autowired
	UserService userService;
	
	@Autowired 
	CartService cartService;
	
	@Autowired
	JavaMailSender javaMailSender;
	
	@Autowired
	EmailService emailService;
	
	  @Autowired
	  TokenGenerator tokenGenerator;
	  
	@GetMapping(value = "/register")
	public String showRegister() {
		return "User/register";
	}
	
	@PostMapping("/register")
	public String register( Model model,@RequestParam("email")String email ,@RequestParam("password")String password ,@RequestParam("username")String username) {	    
		User existingUser = userService.findByEmail(email);
		if(existingUser != null) {
			model.addAttribute("res", new Res(true ,"Địa chỉ email này đã được sử dụng!"));
			return "User/register";
		}
	    
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setUsername(username);
		user.setRole("USER");
		user.setEnabled(false);
		
		String token = tokenGenerator.generateToken();
        user.setToken(token);
        
        String subject = "Xác thực tài khoản";
        String text = "Mã token của bạn là: " + token;
        try {
        	 emailService.sendEmail(email, subject, text);
		} catch (Exception e) {
			e.printStackTrace();
		}
       

		try {
			userService.save(user);
			cartService.getOrCreateCart(user);
			return "redirect:/confirm" ;
		}catch (Exception e) {
			model.addAttribute("res", new Res(true, e.getMessage()));
			return "User/register";
		}
	}

	@GetMapping("/confirm")
	public String showConfirm( Model model) {
		return "User/confirm";
	}
	
	@PostMapping("/confirm")
	public String confirmToken(@RequestParam("token")String token, Model model) {
		User user = userService.findByToken(token);
		if(user != null) {
			
			user.setEnabled(true);
			userService.save(user);
			return "redirect:/login";
		}else {
			model.addAttribute("res", new Res(true ,"Mã token này không hợp lệ!"));
			return "/confirm";
		}
		
		
	}
	

}
