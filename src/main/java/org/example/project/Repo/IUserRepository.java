package org.example.project.Repo;
import org.example.project.Models.UserModel;
import org.springframework.data.repository.CrudRepository;

public interface IUserRepository extends CrudRepository<UserModel, Long> {
    UserModel findByUsername(String username);
}
