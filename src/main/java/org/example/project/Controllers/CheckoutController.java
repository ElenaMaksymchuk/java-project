package org.example.project.Controllers;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.project.Models.*;
import org.example.project.Repo.IUserOrderRepository;
import org.example.project.Repo.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.stream.Collectors;
import org.example.project.Repo.IOrderRepository;
import jakarta.servlet.http.Cookie;

@Controller
public class CheckoutController {

    @Autowired
    private IOrderRepository iOrderRepository;
    @Autowired
    private IUserOrderRepository iUserOrderRepository;
    @Autowired
    private IUserRepository iUserRepository;

    private int calculateTotalCost(List<CartModel> cart) {
        int totalCost = 0;
        for (CartModel item : cart) {
            totalCost += item.getProduct().getPrice() * item.getQuantity();
        }
        return totalCost;
    }

    @GetMapping("/checkout")
    public String showCheckout(HttpSession session, Model model) {
        List<CartModel> cart = (List<CartModel>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return "cart";
        }
        model.addAttribute("totalCost", calculateTotalCost(cart));
        return "checkout";
    }

    @PostMapping("/confirmOrder")
    public String confirmOrder(@RequestParam String fullName, @RequestParam String phone, @RequestParam String address, HttpSession session, Model model) {
        List<CartModel> cart = (List<CartModel>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return "cart";
        }
        OrderModel order = new OrderModel();
        order.setFullName(fullName);
        order.setPhone(phone);
        order.setAddress(address);
        List<OrderItemModel> items = cart.stream().map(cartItem -> {
            OrderItemModel item = new OrderItemModel();
            item.setProduct(cartItem.getProduct());
            item.setQuantity(cartItem.getQuantity());
            item.setOrder(order);
            return item;
        }).collect(Collectors.toList());
        order.setItems(items);
        iOrderRepository.save(order);
        session.removeAttribute("cart");
        model.addAttribute("successMessage", "Ваше замовлення було успішно оформлено!");
        return "confirmOrder";
    }

    @PostMapping("/confirmUserOrder")
    public String confirmUserOrder(HttpServletRequest request, HttpSession session, Model model) {
        List<CartModel> cart = (List<CartModel>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return "cart";
        }
        String username = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    username = cookie.getValue();
                }
            }
        }
        if (username == null) {
            model.addAttribute("errorMessage", "Виникла помилка. Спробуйте ще раз.");
            return "cart";
        }
        UserModel user = iUserRepository.findByUsername(username);
        UserOrderModel order = new UserOrderModel();
        order.setUser(user);
        List<UserOrderItemModel> items = cart.stream().map(cartItem -> {
            UserOrderItemModel item = new UserOrderItemModel();
            item.setProduct(cartItem.getProduct());
            item.setQuantity(cartItem.getQuantity());
            item.setOrder(order);
            return item;
        }).collect(Collectors.toList());
        order.setItems(items);
        iUserOrderRepository.save(order);
        session.removeAttribute("cart");
        model.addAttribute("successMessage", "Ваше замовлення було успішно оформлено!");
        return "confirmOrder";
    }
}


