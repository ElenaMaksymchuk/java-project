package org.example.project.Interceptors;
import org.example.project.Models.CartModel;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Component
public class CartInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        List<CartModel> cart = (List<CartModel>) session.getAttribute("cart");
        int totalQuantity = 0;
        if (cart != null) {
            for (CartModel item : cart) {
                totalQuantity += item.getQuantity();
            }
        }
        request.setAttribute("totalQuantity", totalQuantity);
        return true;
    }
}



