package org.example.project.Models;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class UserOrderModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<UserOrderItemModel> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public List<UserOrderItemModel> getItems() {
        return items;
    }

    public void setItems(List<UserOrderItemModel> items) {
        this.items = items;
    }
}

