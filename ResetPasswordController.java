package com.example.duanjava6.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ResetPasswordController {

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/forgot-password")
    public String showForgotPassword() {
        return "forgotpassword";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Đặt lại mật khẩu");
        message.setText("Nhấp vào liên kết để đặt lại mật khẩu: http://localhost:8080/reset-password?email=" + email);
        mailSender.send(message);
        model.addAttribute("message", "Liên kết đặt lại mật khẩu đã được gửi đến email của bạn.");
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPassword(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("email") String email, @RequestParam("password") String password, Model model) {
        // Logic cập nhật mật khẩu trong UserDAO (mã hóa mật khẩu bằng BCrypt)
        model.addAttribute("message", "Mật khẩu đã được đặt lại thành công!");
        return "login";
    }
}
