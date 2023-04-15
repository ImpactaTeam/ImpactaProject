package Fair.Fruit.FeiraOnline.repository;

import Fair.Fruit.FeiraOnline.entities.UserGP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserGP, Integer> {
    Optional<UserGP> findByLogin(String login);
}
