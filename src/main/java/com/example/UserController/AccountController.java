package com.example.UserController;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.aspectj.apache.bcel.classfile.Module.Require;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.Address;
import com.example.Entity.Order;
import com.example.Entity.OrderDetail;
import com.example.Entity.User;
import com.example.Service.AddressService;
import com.example.Service.DiscountService;
import com.example.Service.OrderDetailService;
import com.example.Service.OrderService;
import com.example.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AccountController {

	@Autowired
	AddressService addressService;

	@Autowired
	UserService userService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	OrderDetailService orderDetailService;
	
	@Autowired
	DiscountService discountService;

	@GetMapping("/information")
	public String showInfo(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		if (user != null) {
			List<Address> addresses = addressService.findByUserId(user.getId());
			model.addAttribute("addresses", addresses);
			model.addAttribute("user", user);
			return "User/account/information";
		} else {
			return "redirect:/login";
		}

	}

	@GetMapping("/changeInfo")
	public String ChangeInfo(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if (user != null) {
			model.addAttribute("user", user);
			return "User/account/changeInfo";
		}
		return "redirect:/login";
	}

	@PostMapping("/changeInfo/save")
	public String updateInfo(Model model, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, HttpServletRequest request,
			@RequestParam(value = "username", required = false) Optional<String> username,
			@RequestParam(value = "phoneNumber", required = false) Optional<String> phoneNumber) {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
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
				if(username.isPresent()) {
					user.setUsername(username.get());
				}
				if(phoneNumber.isPresent()) {
					user.setPhoneNumber(phoneNumber.get());
				}
				userService.save(user);
				return "redirect:/information";
		 }catch (Exception e) {
			 redirectAttributes.addFlashAttribute("danger", "Lưu thông tin thất bại!");
			 return "redirect:/changeInfo";
		}
	}

	
	@GetMapping("/order")
	public String showOrder(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if (user != null) {
			model.addAttribute("user", user);
			List<Order> orders = orderService.findByUserId(user.getId());
			orders = orders.stream().sorted(Comparator.comparing(Order::getOrderDate).reversed())
					.collect(Collectors.toList());
			model.addAttribute("orders", orders);
			List<OrderDetail> orderDetails = new ArrayList<OrderDetail>(); 
			for(Order order : orders) {
				orderDetails = orderDetailService.findByOrderId(order.getId());
				 System.out.println("Order ID: " + order.getId() + ", hasDiscountApplied: " + order.hasDiscountApplied());
			}
			model.addAttribute("orderDetails", orderDetails);
			return "User/account/order";
		}
		return "redirect:/login";
		
	}
	
	@PostMapping("/order/received")
	public String received(@RequestParam("orderId") int orderId) {
		try {
			orderService.updateOrderStatus(orderId, "RECEIVED");
			return "redirect:/order";
		}catch (Exception e) {
			return "redirect:/order";
		}
		
	}
	
	@PostMapping("/order/cancelled")
	public String cancelled(@RequestParam("orderId") int orderId) {
		try {
			orderService.updateOrderStatus(orderId, "CANCELLED");
			return "redirect:/order";
		} catch (Exception e) {
			return "redirect:/order";
		}
	}
	
	
	
	@GetMapping("/address")
	public String EditAddress() {
		return "User/account/address";
	}

	@PostMapping("/address/save-or-update")
	public String saveOrUpdateAddress(Model model, @ModelAttribute Address address, HttpServletRequest request) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		try {
			address.setUser(user);
			System.out.println(user.getId());
			addressService.save(address);
			return "redirect:/information";
		} catch (Exception e) {
			return "redirect:/address";
		}
	}
	
	@GetMapping("/address/delete/{id}")
	public String deleteAddress(@PathVariable int id) {
		try {
			 addressService.delete(id);
			 return "redirect:/information";
		}catch (Exception e) {
			return "redirect:/information";
		}
	}
}
