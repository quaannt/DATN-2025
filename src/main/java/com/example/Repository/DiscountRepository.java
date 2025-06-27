package com.example.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.DiscountCode;

@Repository
public interface DiscountRepository extends JpaRepository<DiscountCode, Integer>{

	DiscountCode findByCode(String code);

}
