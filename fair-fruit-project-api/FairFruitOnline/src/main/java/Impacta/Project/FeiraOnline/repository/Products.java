package Impacta.Project.FeiraOnline.repository;

import Impacta.Project.FeiraOnline.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Products extends JpaRepository<Product, Integer> {
}
