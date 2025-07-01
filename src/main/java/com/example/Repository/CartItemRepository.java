package com.example.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Entity.Cart;
import com.example.Entity.CartItem;
import com.example.Entity.CartItemInfo;
import com.example.Entity.Product;

import jakarta.transaction.Transactional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer>{

	 CartItem findByCartAndProduct(Cart cart, Product product);
	 List<CartItem> findByCart(Cart cart);
	 
	 List<CartItem> findAllByProductId(int productId);
	 
	 @Query("SELECT c FROM CartItem c WHERE c.product.id = :productId AND c.cart.id = :cartId")
	 List<CartItem> findByProductIdAndCartId(@Param("productId") int productId,@Param("cartId") int cartId);
	 
	 
	 
	 @Query("SELECT new com.example.Entity.CartItemInfo(o.product.id, count(o.quantity) as quantity, sum(o.totalAmount) as totalAmount) FROM CartItem o WHERE o.cart.id = :cartId GROUP BY o.product.id")
	 public List<CartItemInfo> getCartItemGroup(@Param("cartId") int cartId);

	 @Modifying
	 @Transactional
	    @Query("DELETE FROM CartItem c WHERE c.product.id = :productId AND c.cart.id = :cartId")
	    void deleteByProductIdAndCartId(@Param("productId") int productId, @Param("cartId") int cartId);
	 
	 @Modifying
	 @Transactional
	 @Query("DELETE FROM CartItem c WHERE c.cart = :cart")
	 void deleteByCart(@Param("cart") Cart cart);
	 
	 @Modifying
	 @Transactional
	 @Query("DELETE FROM CartItem c WHERE c.id = :id")
	 void deleteById(@Param("id") int id);
}
