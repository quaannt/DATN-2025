package com.example.duanjava6.Controller;

import com.example.duanjava6.Dao.UserDAO;
import com.example.duanjava6.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession; // Thêm import này

@Controller
public class LoginController {
    
    @Autowired
    private UserDAO userDAO;

    // Hiển thị form login
    @GetMapping("/login")
    public String showLoginPage(@ModelAttribute("user") User user) {
        return "login";
    }

    // Xử lý login
    @PostMapping("/login")
    public String processLogin(@ModelAttribute("user") User user, Model model, HttpSession session) {
        // Tìm user theo email
        User existingUser = userDAO.findByEmail(user.getEmail())
            .orElse(null);
        
        if (existingUser == null || !existingUser.getPassword().equals(user.getPassword())) {
            model.addAttribute("error", "Email hoặc mật khẩu không đúng!");
            return "login";
        }

        // Lưu thông tin user vào session
        session.setAttribute("loggedInUser", existingUser);

        // Login thành công, chuyển hướng đến trang chủ
        return "redirect:/";
    }
}