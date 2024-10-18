package org.example.project.Repo;
import org.example.project.Models.UserModel;
import org.example.project.Models.UserOrderModel;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface IUserOrderRepository extends CrudRepository<UserOrderModel, Long> {
    List<UserOrderModel> findByUser(UserModel user);
}
