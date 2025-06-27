package com.example.Entity;

import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.annotation.Generated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "username", length = 100, nullable = false)
	private String username;
	
	@Column(name = "password", length = 100, nullable = false)
	private String password;
	
	@Column(name = "email", length = 100, nullable = false)
	private String email;
	
	@Column(name = "image", length = 500, nullable = true)
	private String image;
	
	@Column(name = "role", length = 100, nullable = false)
	private String role;
	
	@Column(name = "phone_number", length = 100, nullable = true)
	private String phoneNumber;
	
	@Column(name = "enabled", nullable = false)
	private boolean enabled;
	
	@Column(name = "token", length = 10, nullable = true)
	private String token;
	
	@OneToOne(mappedBy = "user",cascade = CascadeType.REMOVE)
	private Cart cart;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	private List<Order> orders;
	

	
	@OneToMany(mappedBy = "user")
	private List<Address> address;
}
