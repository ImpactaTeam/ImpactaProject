package Fair.Fruit.FeiraOnline.repository;

import Fair.Fruit.FeiraOnline.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItems extends JpaRepository<OrderItem, Integer> {
}
