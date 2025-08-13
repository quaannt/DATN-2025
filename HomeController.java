package com.example.duanjava6.Controller;

import com.example.duanjava6.Dao.CategoryDAO;
import com.example.duanjava6.Dao.ProductDAO;
import com.example.duanjava6.Entity.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Controller
public class HomeController {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @GetMapping("/")
    public String home(Model model, 
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "categoryId", required = false) Integer categoryId,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = "8") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage;
        if (keyword != null && !keyword.isEmpty()) {
            productPage = productDAO.findByNameContaining(keyword, pageable);
        } else if (categoryId != null) {
            productPage = productDAO.findByCategoryCategoryId(categoryId, pageable);
        } else {
            productPage = productDAO.findAll(pageable);
        }
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("categories", categoryDAO.findAll());
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        return "index";
    }
}