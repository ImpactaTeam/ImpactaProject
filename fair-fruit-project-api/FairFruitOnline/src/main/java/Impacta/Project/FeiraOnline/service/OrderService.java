package Impacta.Project.FeiraOnline.service;

import Impacta.Project.FeiraOnline.dto.OrderDTO;
import Impacta.Project.FeiraOnline.entities.Order;
import Impacta.Project.FeiraOnline.entities.enums.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Order save(OrderDTO dto );
    Optional<Order> getCompleteOrder(Integer id);

    List<Order> getAllCompleteOrder(Integer id);
    void StatusUpdate(Integer id, OrderStatus orderStatus);


}
