package com.example.duanjava6.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.transaction.annotation.Transactional;

import com.example.duanjava6.Dao.UserDAO;
import com.example.duanjava6.Dao.OrderDAO;
import com.example.duanjava6.Dao.OrderDetailDAO;
import com.example.duanjava6.Entity.Order;
import com.example.duanjava6.Entity.OrderDetail;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private OrderDetailDAO orderDetailDAO;

    @GetMapping("/users")
    public String listUsers(Model model) {
        System.out.println("Số lượng người dùng: " + userDAO.findAll().size());
        model.addAttribute("users", userDAO.findAll());
        return "user-list"; // Tên file template: user-list.html
    }

    @PostMapping("/users/delete/{userId}")
    @Transactional
    public String deleteUser(@PathVariable("userId") Integer userId) {
        if (userDAO.existsById(userId)) {
            // 1. Lấy danh sách các Order của User
            List<Order> userOrders = orderDAO.findByUserUserId(userId);

            // 2. Xóa tất cả OrderDetail liên quan đến các Order của User
            for (Order order : userOrders) {
                List<OrderDetail> orderDetails = orderDetailDAO.findByOrderOrderId(order.getOrderId());
                if (!orderDetails.isEmpty()) {
                    orderDetailDAO.deleteAll(orderDetails); // Xóa tất cả OrderDetail của Order này
                    orderDetailDAO.flush(); // Đảm bảo xóa ngay lập tức trong cơ sở dữ liệu
                }
            }

            // 3. Xóa tất cả Order của User
            if (!userOrders.isEmpty()) {
                orderDAO.deleteAll(userOrders);
                orderDAO.flush(); // Đảm bảo xóa ngay lập tức trong cơ sở dữ liệu
            }

            // 4. Xóa User
            userDAO.deleteById(userId);

            System.out.println("Đã xóa người dùng với ID: " + userId + " cùng các Order và OrderDetail liên quan");
        } else {
            System.out.println("Không tìm thấy người dùng với ID: " + userId);
        }
        return "redirect:/users"; // Chuyển hướng về danh sách người dùng sau khi xóa
    }
}