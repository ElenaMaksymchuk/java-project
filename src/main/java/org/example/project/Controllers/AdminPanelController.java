package org.example.project.Controllers;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.example.project.Models.*;
import org.example.project.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdminPanelController {
    @Autowired
    private IProductRepository iProductRepository;
    @Autowired
    private IBrandRepository iBrandRepository;
    @Autowired
    private ICategoryRepository iCategoryRepository;
    @Autowired
    private IUserRepository iUserRepository;
    @Autowired
    private IUserOrderRepository iUserOrderRepository;

    @GetMapping("/adminPanel")
    public String adminPanel(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName()) && "admin".equals(cookie.getValue())) {
                    return "adminPanel";
                }
            }
        }
        return "redirect:/";
    }

    @GetMapping("/adminPanelProducts")
    public String showProducts(HttpServletRequest request, Model model) {
        String username = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName()) && "admin".equals(cookie.getValue())) {
                    username = cookie.getValue();
                    break;
                }
            }
        }
        if (username == null) {
            return "redirect:/";
        }
        List<ProductModel> products = (List<ProductModel>) iProductRepository.findAll();
        model.addAttribute("products", products);
        return "adminPanelProducts";
    }

    @GetMapping("/addProduct")
    public String showAddProduct(HttpServletRequest request, Model model) {
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
        if (username == null || !username.equals("admin")) {
            return "redirect:/";
        }
        List<BrandModel> brands = (List<BrandModel>) iBrandRepository.findAll();
        List<CategoryModel> categories = (List<CategoryModel>) iCategoryRepository.findAll();
        model.addAttribute("brands", brands);
        model.addAttribute("categories", categories);
        return "addProduct";
    }

    @PostMapping("/addProduct")
    public String addProduct(@RequestParam String name, @RequestParam Double price, @RequestParam String size, @RequestParam String color, @RequestParam String description, @RequestParam String image, @RequestParam Long brandId, @RequestParam Long categoryId) {
        ProductModel product = new ProductModel();
        product.setName(name);
        product.setPrice(price);
        product.setSize(size);
        product.setColor(color);
        product.setDescription(description);
        product.setImage(image);
        product.setBrand(iBrandRepository.findById(brandId).orElse(null));
        product.setCategory(iCategoryRepository.findById(categoryId).orElse(null));
        iProductRepository.save(product);
        return "redirect:/adminPanelProducts";
    }

    @GetMapping("/updateProduct")
    public String showUpdateProduct(@RequestParam Long id, HttpServletRequest request, Model model) {
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
        if (username == null || !username.equals("admin")) {
            return "redirect:/";
        }
        ProductModel product = iProductRepository.findById(id).orElse(null);
        if (product == null) {
            return "redirect:/adminPanelProducts";
        }
        List<BrandModel> brands = (List<BrandModel>) iBrandRepository.findAll();
        List<CategoryModel> categories = (List<CategoryModel>) iCategoryRepository.findAll();
        model.addAttribute("product", product);
        model.addAttribute("brands", brands);
        model.addAttribute("categories", categories);
        return "updateProduct";
    }

    @PostMapping("/updateProduct")
    public String updateProduct(@RequestParam Long id, @RequestParam String name, @RequestParam Double price, @RequestParam String size, @RequestParam String color, @RequestParam String description, @RequestParam String image, @RequestParam Long brandId, @RequestParam Long categoryId) {
        ProductModel product = iProductRepository.findById(id).orElse(null);
        if (product != null) {
            product.setName(name);
            product.setPrice(price);
            product.setSize(size);
            product.setColor(color);
            product.setDescription(description);
            product.setImage(image);
            product.setBrand(iBrandRepository.findById(brandId).orElse(null));
            product.setCategory(iCategoryRepository.findById(categoryId).orElse(null));
            iProductRepository.save(product);
        }
        return "redirect:/adminPanelProducts";
    }

    @GetMapping("/deleteProduct")
    public String deleteProduct(@RequestParam Long id) {
        iProductRepository.deleteById(id);
        return "redirect:/adminPanelProducts";
    }

    @GetMapping("/adminPanelCategories")
    public String showCategories(HttpServletRequest request, Model model) {
        String username = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName()) && "admin".equals(cookie.getValue())) {
                    username = cookie.getValue();
                    break;
                }
            }
        }
        if (username == null) {
            return "redirect:/";
        }
        List<CategoryModel> categories = (List<CategoryModel>) iCategoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "adminPanelCategories";
    }

    @GetMapping("/addCategory")
    public String showAddCategory(HttpServletRequest request, Model model) {
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
        if (username == null || !username.equals("admin")) {
            return "redirect:/";
        }
        return "addCategory";
    }

    @PostMapping("/addCategory")
    public String addCategory(@RequestParam String name) {
        CategoryModel category = new CategoryModel();
        category.setName(name);
        iCategoryRepository.save(category);
        return "redirect:/adminPanelCategories";
    }

    @GetMapping("/updateCategory")
    public String showUpdateCategory(@RequestParam Long id, HttpServletRequest request, Model model) {
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
        if (username == null || !username.equals("admin")) {
            return "redirect:/";
        }
        CategoryModel category = iCategoryRepository.findById(id).orElse(null);
        if (category == null) {
            return "redirect:/adminPanelCategories";
        }
        model.addAttribute("category", category);
        return "updateCategory";
    }

    @PostMapping("/updateCategory")
    public String updateCategory(@RequestParam Long id, @RequestParam String name) {
        CategoryModel category = iCategoryRepository.findById(id).orElse(null);
        if (category != null) {
            category.setName(name);
            iCategoryRepository.save(category);
        }
        return "redirect:/adminPanelCategories";
    }

    @GetMapping("/deleteCategory")
    public String deleteCategory(@RequestParam Long id) {
        iCategoryRepository.deleteById(id);
        return "redirect:/adminPanelCategories";
    }

    @GetMapping("/adminPanelUsers")
    public String showUsers(HttpServletRequest request, Model model) {
        String username = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName()) && "admin".equals(cookie.getValue())) {
                    username = cookie.getValue();
                    break;
                }
            }
        }
        if (username == null) {
            return "redirect:/";
        }
        List<UserModel> users = (List<UserModel>) iUserRepository.findAll();
        model.addAttribute("users", users);
        return "adminPanelUsers";
    }

    @PostMapping("/blockUser")
    public String blockUser(@RequestParam Long id, @RequestParam boolean isBlocked, RedirectAttributes redirectAttributes) {
        UserModel user = iUserRepository.findById(id).orElse(null);
        if (user != null) {
            if ("admin".equals(user.getUsername())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Неможливо заблокувати адміністратора.");
            } else {
                user.setBlocked(isBlocked);
                iUserRepository.save(user);
            }
        }
        return "redirect:/adminPanelUsers";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        UserModel user = iUserRepository.findById(id).orElse(null);
        if (user != null) {
            if ("admin".equals(user.getUsername())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Неможливо видалити адміністратора.");
            } else {
                try {
                    iUserRepository.deleteById(id);
                } catch (DataIntegrityViolationException e) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Неможливо видалити користувача, оскільки у нього є замовлення.");
                }
            }
        }
        return "redirect:/adminPanelUsers";
    }

    @GetMapping("/userOrderHistory")
    public String showUserOrderHistory(@RequestParam Long userId, HttpServletRequest request, Model model) {
        String username = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName()) && "admin".equals(cookie.getValue())) {
                    username = cookie.getValue();
                    break;
                }
            }
        }
        if (username == null) {
            return "redirect:/";
        }
        UserModel user = iUserRepository.findById(userId).orElse(null);
        if (user == null) {
            return "redirect:/adminPanelUsers";
        }
        List<UserOrderModel> orders = iUserOrderRepository.findByUser(user);
        model.addAttribute("ordersWithTotalPrice", orders.stream().map(order -> {
            int totalPrice = order.getItems().stream()
                    .mapToInt(item -> (int) (item.getProduct().getPrice() * item.getQuantity()))
                    .sum();
            return new OrderWithTotalPrice(order, totalPrice);
        }).collect(Collectors.toList()));
        return "userOrderHistory";
    }

    @GetMapping("/adminPanelUserOrders")
    public String showUserOrders(@RequestParam Long id, Model model) {
        UserModel user = iUserRepository.findById(id).orElse(null);
        if (user == null) {
            return "redirect:/adminPanelUsers";
        }
        List<UserOrderModel> orders = iUserOrderRepository.findByUser(user);
        List<OrderWithTotalPrice> ordersWithTotalPrice = orders.stream()
                .map(order -> new OrderWithTotalPrice(order, order.getItems().stream()
                        .mapToInt(item -> (int) (item.getProduct().getPrice() * item.getQuantity())).sum()))
                .collect(Collectors.toList());
        model.addAttribute("ordersWithTotalPrice", ordersWithTotalPrice);
        return "adminPanelUserOrders";
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
