package org.example.project.Controllers;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
public class LoginController {

    @Autowired
    private IUserRepository iUserRepository;

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    return "redirect:/";
                }
            }
        }
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username, HttpServletResponse response, @RequestParam String password, Model model) {
        UserModel user = iUserRepository.findByUsername(username);
        if (user != null) {
            if (user.isBlocked()) {
                model.addAttribute("errorMessage", "Користувач " + username + " заблокований!");
                return "login";
            }
            if (user.getPassword().equals(password)) {
                Cookie loginCookie = new Cookie("username", username);
                loginCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(loginCookie);
                return "redirect:/";
            } else {
                model.addAttribute("errorMessage", "Неправильний пароль!");
                return "login";
            }
        } else {
            model.addAttribute("errorMessage", "Користувача не знайдено!");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie loginCookie = new Cookie("username", null);
        loginCookie.setMaxAge(0);
        response.addCookie(loginCookie);
        return "redirect:/";
    }
}
