package com.example.DashboardController;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.Category;
import com.example.Entity.Shipment;
import com.example.Entity.User;
import com.example.Service.ShipmentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/dashboard")
public class ShipmentController {

	@Autowired
	ShipmentService shipmentService;
	
	@GetMapping("/shipment")
	public String shoưPayment(Model model, HttpSession session ) {
		User user = (User) session.getAttribute("user");
		if(user != null && (user.getRole().equals("ADMIN") || user.getRole().equals("EMPLOYEE"))) {
			List<Shipment> shipments = shipmentService.findAll();
			model.addAttribute("shipments", shipments);
			Shipment shipment = new Shipment();
			model.addAttribute("shipment", shipment);
			return "Dashboard/shipment";
		}else {
			return "redirect:/login";
		}
		
	}
	
	@PostMapping("shipment/save-or-update")
	public String saveOrUpdate(Model model, @ModelAttribute Shipment shipment,RedirectAttributes redirectAttributes) {
		List<Shipment> shipments = shipmentService.findAll();
		model.addAttribute("shipments", shipments);
		try {
			shipmentService.save(shipment);
			redirectAttributes.addFlashAttribute("success", "Lưu vận chuyển thành công!");
			return "redirect:/dashboard/shipment";
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Lưu vận chuyển thất bại!");
			return "redirect:/dashboard/shipment";
		}
	}
	
	@GetMapping("/shipment/{id}")
	public String getShipment(Model model, @PathVariable int id) {
		List<Shipment> shipments = shipmentService.findAll();
		model.addAttribute("shipments", shipments);

		Shipment shipmentGet = shipmentService.findById(id);
		Shipment shipment = new Shipment();
		if (shipmentGet != null) {
			shipment = shipmentGet;
		}
		model.addAttribute("shipment", shipment);
		return "Dashboard/shipment";
	}
	
	@GetMapping("/shipment/delete/{id}")
	public String deleteShip(@PathVariable int id, RedirectAttributes redirectAttributes) {
		try {
			shipmentService.delete(id);
			redirectAttributes.addFlashAttribute("success", "Xóa vận chuyển thành công!");
			return "redirect:/dashboard/shipment";
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Xóa vận chuyển thất bại!");
			return "redirect:/dashboard/shipment";
		}
	}
}
