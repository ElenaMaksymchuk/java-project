package org.example.project.Controllers;
import jakarta.servlet.http.HttpServletRequest;
import org.example.project.Models.UserModel;
import org.example.project.Repo.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.Cookie;

@Controller
public class RegistrationController {
    @Autowired
    private IUserRepository userRepository;

    @GetMapping("/registration")
    public String registration(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    return "redirect:/";
                }
            }
        }
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password, @RequestParam String fullName, @RequestParam String address, @RequestParam String phone, @RequestParam String email, Model model) {
        if (userRepository.findByUsername(username) != null) {
            model.addAttribute("errorMessage", "Користувач з таким логіном вже існує!");
            return "registration";
        }
        UserModel newUser = new UserModel();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setFullName(fullName);
        newUser.setAddress(address);
        newUser.setPhone(phone);
        newUser.setEmail(email);
        userRepository.save(newUser);
        model.addAttribute("successMessage", "Користувач " + username + " успішно зареєстрований!");
        return "registration";
    }
}
