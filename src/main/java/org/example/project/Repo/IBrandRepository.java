package org.example.project.Repo;
import org.example.project.Models.BrandModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
public interface IBrandRepository extends CrudRepository<BrandModel, Long> {
    @Query("SELECT p FROM BrandModel p")
    List<BrandModel> findAllBrands();
}
