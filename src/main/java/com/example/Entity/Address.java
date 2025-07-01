package com.example.Entity;

import java.util.List;

import org.apache.el.parser.AstFalse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "addresses")
public class Address {

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
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
}
