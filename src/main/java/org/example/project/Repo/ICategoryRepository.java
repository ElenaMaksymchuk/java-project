package org.example.project.Repo;
import org.example.project.Models.CategoryModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ICategoryRepository extends CrudRepository<CategoryModel, Long> {
    @Query("SELECT p FROM CategoryModel p")
    List<CategoryModel> findAllCategories();
}
