package com.example.duanjava6.Controller;

import com.example.duanjava6.Dao.CategoryDAO;
import com.example.duanjava6.Dao.ProductDAO;
import com.example.duanjava6.Entity.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @GetMapping
    public String getAllProducts(Model model) {
        model.addAttribute("products", productDAO.findAll());
        model.addAttribute("categories", categoryDAO.findAll());
        return "product";
    }

    @GetMapping("/product") // Thêm để xử lý /products/product
    public String getAllProductsAlternate(Model model) {
        model.addAttribute("products", productDAO.findAll());
        model.addAttribute("categories", categoryDAO.findAll());
        return "product";
    }

    @GetMapping("/product/{id}")
    public String getProductDetail(@PathVariable("id") Integer id, Model model) {
        Product product = productDAO.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid product Id"));
        model.addAttribute("product", product);
        return "product-detail";
    }
}