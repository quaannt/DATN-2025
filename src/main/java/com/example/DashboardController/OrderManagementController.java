package com.example.DashboardController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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

import com.example.Entity.Order;
import com.example.Entity.OrderDetail;
import com.example.Entity.User;
import com.example.Service.OrderDetailService;
import com.example.Service.OrderService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/dashboard")
public class OrderManagementController {

	@Autowired
	OrderService orderService;

	@Autowired
	OrderDetailService orderDetailService;

	@GetMapping("/order")
	public String showOrder(HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		List<Order> orders = orderService.findAll();
		orders = orders.stream().sorted(Comparator.comparing(Order::getOrderDate).reversed())
				.collect(Collectors.toList());
		model.addAttribute("orders", orders);
		if (user != null && (user.getRole().equals("ADMIN") || user.getRole().equals("EMPLOYEE"))) {
			return "Dashboard/order";
		} else {
			return "redirect:/login";
		}
	}

	@GetMapping("/order/delete/{id}")
	public String deleteOrder(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {

		try {
			orderService.deleteById(id);
			redirectAttributes.addFlashAttribute("success", "Xóa đơn hàng thành công!");
			return "redirect:/dashboard/order";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Xóa đơn hàng thất bại!");
			return "redirect:/dashboard/order";
		}

	}

	@GetMapping("/order/{id}")
	public String getOrder(@PathVariable int id, Model model, HttpSession session) {

		User user = (User) session.getAttribute("user");

		if (user != null && (user.getRole().equals("ADMIN") || user.getRole().equals("EMPLOYEE"))) {
			Optional<Order> orderGet = orderService.findById(id);
			Order order = new Order();
			List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
			if (orderGet.isPresent()) {
				order = orderGet.get();

				orderDetails = orderDetailService.findByOrderId(order.getId());

			}
			model.addAttribute("order", order);

			model.addAttribute("orderDetails", orderDetails);
			return "Dashboard/orderDetail";
		} else {
			return "redirect:/login";
		}

	}

	@PostMapping("/order/update")
	public String updateOrder(@RequestParam("orderId") int orderId, @RequestParam("orderStatus") String orderStatus,
			@RequestParam("paymentStatus") String paymentStatus, RedirectAttributes redirectAttributes, Model model) {
		try {
			orderService.updateStatus(orderId, orderStatus, paymentStatus);
			redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin đơn hàng thành công!");
			return "redirect:/dashboard/order";
		} catch (Exception e) {
			return "redirect:/dashboard/orderDetail?id=" + orderId;
		}
	}

	@GetMapping("/orderDetail")
	public String showOrderDetail(@RequestParam("id") int id, HttpSession session) {
		User user = (User) session.getAttribute("user");

		if (user != null && (user.getRole().equals("ADMIN") || user.getRole().equals("EMPLOYEE"))) {
			return "Dashboard/orderDetail";
		} else {
			return "redirect:/login";
		}

	}
}
