package com.example.Entity;

import java.time.LocalDateTime;
import java.util.List;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
@Table(name = "products")
public class Product {

	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name", length = 100, nullable = false)
	private String name;
	
	@Column(name = "description", length = 5000, nullable = true)
	private String description;
	
	@Column(name = "price", length = 50, nullable = false)
	private Double price;
	
	@Column(name = "promotional_price", length = 50, nullable = true)
	private Double promotionalPrice;
	
	@Column(name = "image", length = 500, nullable = true)
	private String image;
	
	@Column(name = "created_at", nullable = true)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at", nullable = true)
	private LocalDateTime updatedAt;
	
	@Column(name = "status")
	private boolean status;
	
	@Column(name = "content", columnDefinition = "TEXT")
	@Lob
	private String content;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Review> reviews;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private List<CartItem> cartItems;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<PinnedProduct> pinnedProducts;
	
	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}
	
	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}
	
	public Double getRealPrice() {
		return this.promotionalPrice != null ? this.promotionalPrice : this.price;
	}
}
