package com.example.duanjava6.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.duanjava6.Dao.CategoryDAO;
import com.example.duanjava6.Entity.Category;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryDAO categoryDAO;

    // Hiển thị danh sách danh mục
    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryDAO.findAll());
        return "categories";
    }

    // Hiển thị form thêm danh mục
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        return "category-form";
    }

    // Hiển thị form sửa danh mục
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        Category category = categoryDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Danh mục không tồn tại: " + id));
        model.addAttribute("category", category);
        return "category-form";
    }

    // Lưu danh mục mới
    @PostMapping("/save")
    public String saveCategory(@ModelAttribute("category") Category category) {
        categoryDAO.save(category);
        return "redirect:/categories";
    }

    // Cập nhật danh mục
    @PostMapping("/update")
    public String updateCategory(@ModelAttribute("category") Category category) {
        categoryDAO.save(category);
        return "redirect:/categories";
    }

    // Xóa danh mục
    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer id) {
        if (categoryDAO.existsById(id)) {
            categoryDAO.deleteById(id);
        }
        return "redirect:/categories";
    }
}