package org.example.project.Controllers;
import org.example.project.Models.BrandModel;
import org.example.project.Models.CategoryModel;
import org.example.project.Models.ProductModel;
import org.example.project.Repo.IBrandRepository;
import org.example.project.Repo.ICategoryRepository;
import org.example.project.Repo.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private IProductRepository iProductRepository;
    @Autowired
    private IBrandRepository iBrandRepository;
    @Autowired
    private ICategoryRepository iCategoryRepository;

    @GetMapping("/products")
    public String products (Model model) {
        Iterable<ProductModel> products = iProductRepository.findAll();
        List<BrandModel> brands = iBrandRepository.findAllBrands();
        List<CategoryModel> categories = iCategoryRepository.findAllCategories();
        model.addAttribute("brands", brands);
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        return "products";
    }
    @PostMapping("/products")
    public String products (@RequestParam(value = "name", defaultValue = "") String name, @RequestParam(value = "brand", required = false) Long brandId, @RequestParam(value = "price", required = false) Integer price, @RequestParam(value = "category", required = false) Long categoryId, @RequestParam(value = "size", required = false) String size, @RequestParam(value = "color", required = false) String color, Model model) {
        Iterable<ProductModel> products = iProductRepository.searchProducts(name, brandId, price, categoryId, size, color);
        List<BrandModel> brands = iBrandRepository.findAllBrands();
        List<CategoryModel> categories = iCategoryRepository.findAllCategories();
        model.addAttribute("brands", brands);
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        return "products";
    }
    @GetMapping("/products/{id}")
    public String product (@PathVariable(value = "id") long id, Model model) {
        if (!iProductRepository.existsById(id))
            return "redirect:/products";
        ProductModel productModel = iProductRepository.findById(id).get();
        model.addAttribute("product", productModel);
        return "product";
    }
}
