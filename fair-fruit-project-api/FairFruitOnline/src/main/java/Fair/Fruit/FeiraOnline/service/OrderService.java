package Fair.Fruit.FeiraOnline.service;

import Fair.Fruit.FeiraOnline.dto.OrderDTO;
import Fair.Fruit.FeiraOnline.entities.Order;
import Fair.Fruit.FeiraOnline.entities.enums.OrderStatus;

import java.util.Optional;

public interface OrderService {

    Order save(OrderDTO dto );
    Optional<Order> getCompleteOrder(Integer id);
    void StatusUpdate(Integer id, OrderStatus orderStatus);


}
