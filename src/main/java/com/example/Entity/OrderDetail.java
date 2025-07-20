package com.example.Entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "order_details")
public class OrderDetail {

	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "quantity", length = 11, nullable = false)
	private int quantity;
	
	@Column(name = "total_amount", length = 11, nullable = false)
	private Double totalAmount;
	
	@Column(name = "reviewed")
	private boolean reviewed = false;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;
	
	@ManyToOne
	@JoinColumn(name = "order_product_id")
	private OrderProduct orderProduct;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	
	
	@OneToMany(mappedBy = "orderDetail",  fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<Review> review;
	
	public Double calculateTotalAmount() {
		return this.quantity * this.orderProduct.getPrice();
	}
	
}
