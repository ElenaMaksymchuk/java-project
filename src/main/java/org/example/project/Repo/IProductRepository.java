package org.example.project.Repo;
import org.example.project.Models.ProductModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface IProductRepository extends CrudRepository<ProductModel, Long> {
    @Query("SELECT DISTINCT p FROM ProductModel p LEFT JOIN p.category c LEFT JOIN p.brand b WHERE (:name IS NULL OR :name = '' OR :name = ' ' OR p.name LIKE %:name%) AND (:brandId IS NULL OR b.id = :brandId) AND (:price IS NULL OR :price = '' OR STR(p.price) LIKE %:price%) AND (:categoryId IS NULL OR c.id = :categoryId) AND (:size IS NULL OR :size = '' OR p.size LIKE %:size%) AND (:color IS NULL OR :color = '' OR p.color LIKE %:color%)")
    List<ProductModel> searchProducts(@Param("name") String name, @Param("brandId") Long brandId, @Param("price") Integer price, @Param("categoryId") Long categoryId, @Param("size") String size, @Param("color") String color);
    @Query("SELECT p FROM ProductModel p WHERE p.id = :productId")
    ProductModel findByProductId(Long productId);
}
