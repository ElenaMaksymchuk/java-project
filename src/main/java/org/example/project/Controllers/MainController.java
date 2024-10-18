package org.example.project.Controllers;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String home(Model model) { return "home"; }

    @GetMapping("/about")
    public String about(Model model) {
        return "about";
    }

    @GetMapping("/contacts")
    public String contacts(Model model) {
        return "contacts";
    }
}
