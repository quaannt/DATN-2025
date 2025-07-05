package com.example.Config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.Entity.Support;
import com.example.Entity.User;
import com.example.Service.SupportService;
import com.example.Service.UserService;

@ControllerAdvice
public class GlobalModelAttribute {

	@Autowired
	private SupportService supportService;
	
	
	@ModelAttribute
	public void support(Model model) {
		List<Support> supports = supportService.getAllsSupports();
		model.addAttribute("supports", supports);
	}
	

}
