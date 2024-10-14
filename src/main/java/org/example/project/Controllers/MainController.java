package org.example.project.Controllers;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    @GetMapping("/")
    public String home (@RequestParam (name = "name", required = false, defaultValue = "Elena") String name, Model model) {
        model.addAttribute("name", name);
        return "home";
    }
    @GetMapping("/about")
    public String about (Model model) {
        return "about";
    }
    @GetMapping("/contacts")
    public String contacts (Model model) {
        return "contacts";
    }
}
