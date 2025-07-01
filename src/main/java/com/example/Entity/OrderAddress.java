package com.example.Entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "order_address")
public class OrderAddress {

	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "apartment_number", length = 100, nullable = false)
	private String apartmentNumber;
	
	@Column(name = "ward", length = 100,nullable = false)
	private String ward;
	
	@Column(name = "district", length = 100, nullable = false)
	private String district;
	
	@Column(name = "city", length = 100, nullable = false)
	private String city;
	
	@Column(name = "phone_number", length = 100, nullable = false)
	private String phoneNumber;
	

	@OneToMany(mappedBy = "orderAddress", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<Order> order;
	
}
