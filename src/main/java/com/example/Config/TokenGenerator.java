package com.example.Config;

import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class TokenGenerator {

	public String generateToken() {
		 Random random = new Random();
	        
	        // Tạo một chuỗi để lưu trữ token
	        StringBuilder token = new StringBuilder();
	        
	        // Tạo 4 số ngẫu nhiên và thêm chúng vào chuỗi token
	        for (int i = 0; i < 4; i++) {
	            int randomNumber = random.nextInt(10); // Tạo số ngẫu nhiên từ 0 đến 9
	            token.append(randomNumber); // Thêm số ngẫu nhiên vào chuỗi token
	        }
	        
	        // Trả về chuỗi token
	        return token.toString();
	    }

	  
}
