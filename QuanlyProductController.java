package com.example.duanjava6.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.duanjava6.Dao.CategoryDAO;
import com.example.duanjava6.Dao.ProductDAO;
import com.example.duanjava6.Entity.Product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/QuanlyProduct")
public class QuanlyProductController {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    // Thư mục lưu ảnh
    private static final String UPLOAD_DIR = "C:\\JAVA6\\Jva6\\src\\main\\resources\\static\\Image\\";

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productDAO.findAll());
        return "QuanlyProduct";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryDAO.findAll());
        return "product-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        Product product = productDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại: " + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryDAO.findAll());
        return "product-form";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product,
                              @RequestParam("image") MultipartFile image) throws IOException {
        if (!image.isEmpty()) {
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            product.setImageUrl("/Image/" + fileName); // Lưu đường dẫn tương đối
        }
        productDAO.save(product);
        return "redirect:/QuanlyProduct";
    }

    @PostMapping("/update")
    public String updateProduct(@ModelAttribute("product") Product product,
                                @RequestParam("image") MultipartFile image) throws IOException {
        if (!image.isEmpty()) {
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            product.setImageUrl("/Image/" + fileName); // Cập nhật đường dẫn mới
        } else if (product.getImageUrl() == null) {
            product.setImageUrl(null); // Xóa đường dẫn nếu không có ảnh mới và không giữ ảnh cũ
        }
        productDAO.save(product);
        return "redirect:/QuanlyProduct";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id) {
        if (productDAO.existsById(id)) {
            productDAO.deleteById(id);
        }
        return "redirect:/QuanlyProduct";
    }
}