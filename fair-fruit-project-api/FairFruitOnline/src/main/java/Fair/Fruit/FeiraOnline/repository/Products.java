package Fair.Fruit.FeiraOnline.repository;

import Fair.Fruit.FeiraOnline.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Products extends JpaRepository<Product, Integer> {
}
