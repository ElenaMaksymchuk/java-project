package org.example.project.Controllers;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.example.project.Models.UserModel;
import org.example.project.Models.UserOrderModel;
import org.example.project.Repo.IUserRepository;
import org.example.project.Repo.IUserOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CabinetController {
    @Autowired
    private IUserRepository iUserRepository;
    @Autowired
    private IUserOrderRepository iUserOrderRepository;
    @GetMapping("/cabinet")
    public String cabinet(HttpServletRequest request, Model model) {
        String username = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    username = cookie.getValue();
                    break;
                }
            }
        }
        if (username != null) {
            UserModel user = iUserRepository.findByUsername(username);
            model.addAttribute("user", user);

            List<UserOrderModel> orders = iUserOrderRepository.findByUser(user);
            model.addAttribute("orders", orders);
            model.addAttribute("ordersWithTotalPrice", orders.stream().map(order -> {
                int totalPrice = order.getItems().stream()
                        .mapToInt(item -> (int) (item.getProduct().getPrice() * item.getQuantity()))
                        .sum();
                return new OrderWithTotalPrice(order, totalPrice);
            }).collect(Collectors.toList()));
            return "cabinet";
        }
        return "redirect:/";
    }

    @PostMapping("/updateUser")
    public String updateUser(@RequestParam Long id, @RequestParam String password, @RequestParam String fullName, @RequestParam String address, @RequestParam String phone, @RequestParam String email, RedirectAttributes redirectAttributes) {
        UserModel user = iUserRepository.findById(id).orElse(null);
        if (user != null) {
            user.setPassword(password);
            user.setFullName(fullName);
            user.setAddress(address);
            user.setPhone(phone);
            user.setEmail(email);
            iUserRepository.save(user);
        }
        redirectAttributes.addFlashAttribute("successMessage", "Дані користувача були успішно змінені.");
        return "redirect:/cabinet";
    }

    static class OrderWithTotalPrice {
        private final UserOrderModel order;
        private final int totalPrice;

        public OrderWithTotalPrice(UserOrderModel order, int totalPrice) {
            this.order = order;
            this.totalPrice = totalPrice;
        }

        public UserOrderModel getOrder() {
            return order;
        }

        public int getTotalPrice() {
            return totalPrice;
        }
    }
}
