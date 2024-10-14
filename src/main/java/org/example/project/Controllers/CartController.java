package org.example.project.Controllers;
import jakarta.servlet.http.HttpSession;
import org.example.project.Models.CartModel;
import org.example.project.Models.ProductModel;
import org.example.project.Repo.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {
    @Autowired
    private IProductRepository iProductRepository;

    @GetMapping("/cart")
    public String showCart(HttpSession session, Model model) {
        List<CartModel> cart = (List<CartModel>) session.getAttribute("cart");
        if (cart != null) {
            model.addAttribute("totalCost", calculateTotalCost(cart));
            model.addAttribute("totalQuantity", calculateTotalQuantity(cart));
        } else {
            model.addAttribute("totalCost", 0);
            model.addAttribute("totalQuantity", 0);
        }
        return "cart";
    }

    @PostMapping("/addProductToCart/{productId}")
    public String addProductToCart(@PathVariable Long productId, HttpSession session, Model model) {
        ProductModel productModel = iProductRepository.findByProductId(productId);
        List<CartModel> cart = (List<CartModel>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        for (CartModel cartItem : cart) {
            if (cartItem.getProduct().getId().equals(productModel.getId())) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                session.setAttribute("cart", cart);
                model.addAttribute("totalCost", calculateTotalCost(cart));
                return "redirect:/cart";
            }
        }
        CartModel newCartItem = new CartModel(productModel, 1);
        cart.add(newCartItem);
        session.setAttribute("cart", cart);
        model.addAttribute("totalCost", calculateTotalCost(cart));
        return "redirect:/cart";
    }

    private int calculateTotalCost(List<CartModel> cart) {
        int totalCost = 0;
        for (CartModel item : cart) {
            totalCost += item.getProduct().getPrice() * item.getQuantity();
        }
        return totalCost;
    }

    @PostMapping("/addProduct/{productId}")
    public String addProduct(@PathVariable Long productId, HttpSession session) {
        List<CartModel> cart = (List<CartModel>) session.getAttribute("cart");
        if (cart != null) {
            for (CartModel cartItem : cart) {
                if (cartItem.getProduct().getId().equals(productId)) {
                    cartItem.setQuantity(cartItem.getQuantity() + 1);
                    break;
                }
            }
            session.setAttribute("cart", cart);
        }
        return "redirect:/cart";
    }

    @PostMapping("/removeProduct/{productId}")
    public String removeProduct(@PathVariable Long productId, HttpSession session) {
        List<CartModel> cart = (List<CartModel>) session.getAttribute("cart");
        if (cart != null) {
            for (CartModel cartItem : cart) {
                if (cartItem.getProduct().getId().equals(productId)) {
                    if (cartItem.getQuantity() > 1) {
                        cartItem.setQuantity(cartItem.getQuantity() - 1);
                    } else {
                        cart.remove(cartItem);
                    }
                    break;
                }
            }
            session.setAttribute("cart", cart);
        }
        return "redirect:/cart";
    }

    private int calculateTotalQuantity(List<CartModel> cart) {
        int totalQuantity = 0;
        for (CartModel item : cart) {
            totalQuantity += item.getQuantity();
        }
        return totalQuantity;
    }
}


