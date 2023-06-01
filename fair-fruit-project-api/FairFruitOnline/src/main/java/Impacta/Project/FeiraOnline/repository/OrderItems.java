package Impacta.Project.FeiraOnline.repository;

import Impacta.Project.FeiraOnline.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItems extends JpaRepository<OrderItem, Integer> {
}
