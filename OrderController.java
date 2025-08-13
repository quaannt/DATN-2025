package com.example.duanjava6.Controller;

import com.example.duanjava6.Entity.Order;
import com.example.duanjava6.Dao.OrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = orderRepository.findAll();
        model.addAttribute("orders", orders);
        return "orders"; // Trả về giao diện danh sách đơn hàng
    }

    @PostMapping("/update-status")
    public String updateOrderStatus(@RequestParam Integer orderId, @RequestParam String status) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setOrderStatus(Order.OrderStatus.valueOf(status));
            orderRepository.save(order);
        }
        return "redirect:/orders";
    }

    @PostMapping("/delete")
    public String deleteOrder(@RequestParam Integer orderId) {
        orderRepository.deleteById(orderId);
        return "redirect:/orders";
    }
}
