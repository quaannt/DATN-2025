package com.example.UserController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.Address;
import com.example.Entity.Cart;
import com.example.Entity.CartItem;
import com.example.Entity.DiscountCode;
import com.example.Entity.Order;
import com.example.Entity.OrderAddress;
import com.example.Entity.OrderDetail;
import com.example.Entity.OrderDiscount;
import com.example.Entity.OrderProduct;
import com.example.Entity.OrderShipment;
import com.example.Entity.Product;
import com.example.Entity.Shipment;
import com.example.Entity.User;
import com.example.Service.AddressService;
import com.example.Service.CartService;
import com.example.Service.DiscountService;
import com.example.Service.OrderAddressService;
import com.example.Service.OrderDetailService;
import com.example.Service.OrderDiscountService;
import com.example.Service.OrderProductService;
import com.example.Service.OrderService;
import com.example.Service.OrderShipmentService;
import com.example.Service.ShipmentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {

	@Autowired
	OrderService orderService;

	@Autowired
	CartService cartService;

	@Autowired
	AddressService addressService;

	@Autowired
	ShipmentService shipmentService;

	@Autowired
	OrderDetailService orderDetailService;

	@Autowired
	OrderProductService orderProductService;
	
	@Autowired
	DiscountService discountService;
	
	@Autowired
	OrderShipmentService orderShipmentService;
	
	@Autowired
	OrderDiscountService orderDiscountService;
	
	@Autowired
	OrderAddressService orderAddressService;
	
	@Value("${bank.bank}")
	private String bank;
	
	@Value("${bank.bank.account}")
	private String bankAccount;
	
	@Value("${bank.qr.url}")
	private String qrUrl;
	

	@PostMapping("/process-payment")
	public String processPayment(
	        @RequestParam("shipmentId") int shipmentId,
	        @RequestParam("addressId") int addressId,
	        @RequestParam(name = "note", required = false) String note,
	        @RequestParam("paymentMethod") String paymentMethod,
	        @RequestParam(name = "discountCode", required = false) String discountCode,
	        HttpSession session,
	        RedirectAttributes redirectAttributes, Model model) {

	    User user = (User) session.getAttribute("user");

	    if (user == null) {
	        return "redirect:/login";
	    }

	    try {
	    	// Tính tổng tiền sản phẩm trong giỏ hàng
		    Double totalProductAmount = cartService.calculateTotalForCart(user.getCart().getId());
		    Double shipmentFee = 0.0;
		    Double discountAmount = 0.0;

		    // Lấy thông tin vận chuyển
		    Shipment shipmentSelected = shipmentService.findById(shipmentId);
		    if (shipmentSelected != null) {
		        shipmentFee = shipmentSelected.getPrice();
		    }

		    DiscountCode appliedDiscount = null;
		    
		    Date today = new java.sql.Date(System.currentTimeMillis());
		    
		    System.out.println("discount code:" + discountCode);
		    
		    DiscountCode code = discountService.findByCode(discountCode);
		    if (code != null) {
		       
		        System.out.println("codeId:" + code.getId());
		        if (code.getEffectiveDate().compareTo(today) <= 0 &&
		        	    code.getExpirationDate().compareTo(today) >= 0 &&
		        	    code.getQuantity() > 0) {

		            // Áp dụng giảm giá lên phần giá sản phẩm
		            discountAmount = (totalProductAmount * code.getDiscountValue()) / 100.0;
		            totalProductAmount -= discountAmount;

		            // Trừ số lượng mã giảm giá
		            code.setQuantity(code.getQuantity() - 1);
		            discountService.save(code);

		            appliedDiscount = code;
		        } else {
		            redirectAttributes.addFlashAttribute("danger", "Mã giảm giá không hợp lệ!");
		            return "redirect:/payment";
		        }
		    }

		    // Tính tổng cần thanh toán = sản phẩm đã giảm + phí ship
		    Double totalPayment = totalProductAmount + shipmentFee;

		    // Kiểm tra địa chỉ
		    Optional<Address> addressOptional = addressService.findById(addressId);
		    if (!addressOptional.isPresent()) {
		        redirectAttributes.addFlashAttribute("danger", "Địa chỉ không hợp lệ!");
		        return "redirect:/payment";
		    }

		    Address address = addressOptional.get();

		    // Tạo đơn hàng
		    Order order = new Order();
		    order.setUser(user);
		    
		    OrderAddress orderAddress = new OrderAddress();
		    orderAddressService.createOrderAddress(orderAddress, address);
		    order.setOrderAddress(orderAddress);
		    
		    OrderShipment orderShipment = new OrderShipment();
		    orderShipmentService.createOrderShipment(orderShipment, shipmentSelected);
		    order.setOrderShipment(orderShipment);
		    
		    order.setTotalAmount(totalProductAmount); // tổng tiền sản phẩm sau khi trừ giảm giá
		    order.setTotalPaymentAmount(totalPayment); // tổng thanh toán cuối cùng
		    order.setPaymentMethod(paymentMethod);
		    order.setPaymentStatus("UNPAID");

		    if ("CASH_ONLINE".equals(paymentMethod)) {
		        order.setOrderStatus("WAITING");
		    } else if ("CASH_ON_DELIVERY".equals(paymentMethod)) {
		        order.setOrderStatus("APPLYING");
		    } else {
		        order.setOrderStatus("PENDING");
		    }

		    if (appliedDiscount != null) {
		    	OrderDiscount orderDiscount = new OrderDiscount();
		        orderDiscountService.createOrderDiscount(orderDiscount, appliedDiscount);
		        order.setOrderDiscount(orderDiscount);
		    }

		    order.setNote(note);
		    orderService.save(order);

		    // Thêm chi tiết đơn hàng từ giỏ hàng
		    if (user.getCart() != null) {
		        int cartId = user.getCart().getId();
		        List<CartItem> cartItems = cartService.getCartItemGroup(cartId);

		        for (CartItem cartItem : cartItems) {
		            Product product = cartItem.getProduct();
		            int quantity = cartItem.getTotalQuantity();

		            OrderDetail orderDetail = new OrderDetail();
		            
		            OrderProduct orderProduct = new OrderProduct();
		            orderProductService.createOrderProduct(orderProduct, product);
		            orderDetail.setOrderProduct(orderProduct);
		            orderDetail.setProduct(product);
		            
		            orderDetail.setQuantity(quantity);
		            Double totalAmountProduct = orderDetail.calculateTotalAmount();
		            orderDetail.setTotalAmount(totalAmountProduct);
		            orderDetail.setOrder(order);
		            orderDetail.setReviewed(false);

		            orderDetailService.save(orderDetail);
		        }

		        // Xoá giỏ hàng sau khi đặt hàng
		        cartService.clearCart(cartId);
		    }
		    
		    if("CASH_ONLINE".equals(order.getPaymentMethod())){
		    	
		    	return "redirect:/order/check-pay/" + order.getId();
		    }
		    redirectAttributes.addFlashAttribute("success", "Đặt hàng thành công!");
	    }catch (Exception e) {
	    	e.printStackTrace();
			// TODO: handle exception
	    	redirectAttributes.addFlashAttribute("danger", "Đặt hàng thất bại!");
	    	
		}

	    return "redirect:/index";
	}
	
	@GetMapping("/order/check-pay/{id}")
	public String checkPay (@PathVariable int id, Model model, HttpSession session) {
		
		 User user = (User) session.getAttribute("user");

		    if (user == null) {
		        return "redirect:/login";
		    }
		    
		    
		try {
			Order order = orderService.findById(id).orElse(null);
			model.addAttribute("order", order);
			String QR = qrUrl + "?acc=" + bankAccount + "&bank=" + bank + "&amount=" + order.getTotalPaymentAmount() + "&des=" + order.getId();
	    	model.addAttribute("QR", QR);
	    	return "User/check-pay";
		}catch (Exception e) {
			
			return "redirect:/index";
			// TODO: handle exception
		}
	}

	
//	@GetMapping("/orderNotification")
//	public String orderNotification() {
//		return "User/orderNotification";
//	}
}
