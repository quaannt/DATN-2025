package com.example.duanjava6.Controller;

import com.example.duanjava6.Entity.User;
import com.example.duanjava6.Dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class SignUpController {
    
    @Autowired
    private UserDAO userDAO;

    // Hiển thị form đăng ký
    @GetMapping("/signup")
    public String showSignUpPage(@ModelAttribute("user") User user) {
        return "signup";
    }

    // Xử lý form đăng ký
    @PostMapping("/signup")
    public String processSignUp(@ModelAttribute("user") User user, Model model) {
        // Kiểm tra xem email đã tồn tại chưa
        if (userDAO.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Email đã được sử dụng!");
            return "signup";
        }
        
        userDAO.save(user);
        return "redirect:/login"; // Chuyển hướng sau khi đăng ký thành công
    }
}