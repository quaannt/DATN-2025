package com.example.Entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "order_date")
	private Date orderDate;
	
	@Column(name = "total_amount", length = 11, nullable = false)
	private Double totalAmount;
	
	@Column(name = "total_payment_amount", length = 11, nullable = false)
	private Double totalPaymentAmount;
	
	@Column(name =  "order_status", length = 100, nullable = false)
	private String orderStatus;
	
	@Column(name = "payment_status", length = 100, nullable = false)
	private String paymentStatus;

	@Column(name = "payment_method", length = 250, nullable = true)
	private String paymentMethod;
	
	@Column(name = "note", columnDefinition = "TEXT")
	@Lob
	private String note;
	
	@ManyToOne
	@JoinColumn(name = "order_discount_id")
	private OrderDiscount orderDiscount;
	
	@ManyToOne
	@JoinColumn(name = "order_shipment_id")
	private OrderShipment orderShipment;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "order_address_id")
	private OrderAddress orderAddress;
	

	@OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<OrderDetail> orderDetails;
	
	
	@PrePersist 
	public void onCreate() {
		orderDate = new Date();
	}

	public void addOrderDetail(OrderDetail orderDetail) {
		if(this.orderDetails == null) {
			this.orderDetails = new ArrayList<>();
		}
		this.orderDetails.add(orderDetail);
		orderDetail.setOrder(this);
	}
 
	public boolean hasDiscountApplied() {
		return orderDiscount != null;
		
	}
}
