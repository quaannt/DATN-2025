package com.example.DashboardController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import com.example.Entity.Support;
import com.example.Entity.User;
import com.example.Service.SupportService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/dashboard")
public class SupportController {
    @Autowired
    private SupportService supportService;
    @Value("${upload.dir}")
    private String uploadDir; 
   
    @Autowired
    private com.example.Config.UploadService uploadService;

    @Value("${static-folder}")
    private String staticFolder;

    @GetMapping("/support")
    public String showgetAllsSupports(Model model, HttpSession session) {
    	User user = (User) session.getAttribute("user");

		if (user == null) {
			return "redirect:/login";
		}
        model.addAttribute("supports", supportService.getAllsSupports());
        return "Dashboard/support";
    }

    @GetMapping("/support/delete/{id}")
    public String deleteSupport(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        try {
            supportService.deleteSupport(id);
            redirectAttributes.addFlashAttribute("success", "Xoá Thành Công");
        } catch (Exception e) {
            // TODO: handle exception
            redirectAttributes.addFlashAttribute("danger", "Xoá Thất bại");
        }
        return "redirect:/dashboard/list";
    }

    @PostMapping("/support/add")
    public String postMethodName(@ModelAttribute Support support,
            @RequestParam(value = "photoFile") MultipartFile photoFile, RedirectAttributes redirectAttributes) {

        try {
            if (!photoFile.isEmpty()) {
                String filePhoto = uploadService.saveFile(photoFile, "system-images"); 
                support.setPhoto(staticFolder + "system-images/" + filePhoto); 
            } else {
                support.setPhoto(null); 
            }
            supportService.SaveSupport(support);
            redirectAttributes.addFlashAttribute("success", "Tạo mới thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("danger", "Tạo mới thất bại");
        }
        return "redirect:/dashboard/support";
    }

    @PostMapping("/support/update")
    public String UpdateSupport(@ModelAttribute Support support,
            @RequestParam(value = "photoFile") MultipartFile photoFile,
            RedirectAttributes redirectAttributes) {
        try {

            Support existing = supportService.findById(support.getId());
           
            if (!photoFile.isEmpty()) {
                String filePhoto = uploadService.saveFile(photoFile, "system-images");
                support.setPhoto(staticFolder + "system-images/" + filePhoto);
            } else {

                support.setPhoto(existing.getPhoto());
            }
            supportService.SaveSupport(support);
            redirectAttributes.addFlashAttribute("success", "Cập Nhật thành công");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("danger", "Cập Nhật thất bại");
        }

        return "redirect:/dashboard/support";

    }

}
