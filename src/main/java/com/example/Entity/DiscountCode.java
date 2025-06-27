package com.example.Entity;

import java.util.Date;
import java.util.List;

import org.apache.el.parser.AstFalse;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "discount_codes")
public class DiscountCode {

	
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "code")
	private String code;
	
	@Column(name = "discount_value")
	private Double discountValue;
	
	@Column(name = "quantity", length = 11, nullable = false)
	private int quantity;
	
	@Column(name = "create_date")
	
	private  Date createDate;
	
	@Column(name = "effective_date")
	private java.sql.Date effectiveDate;
	
	@Column(name = "expiration_date")
	private java.sql.Date expirationDate;
	
	@Column(name = "deleteStatus")
	private Boolean deleteStatus;
	
	@PrePersist
	public void onCreate() {
		createDate = new Date();
	}
}
