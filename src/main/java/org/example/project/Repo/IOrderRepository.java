package org.example.project.Repo;
import org.example.project.Models.OrderModel;
import org.springframework.data.repository.CrudRepository;

public interface IOrderRepository extends CrudRepository<OrderModel, Long> {
}
