package Fair.Fruit.FeiraOnline.service.impl;

import Fair.Fruit.FeiraOnline.dto.OrderDTO;
import Fair.Fruit.FeiraOnline.dto.OrderItemDTO;
import Fair.Fruit.FeiraOnline.entities.Client;
import Fair.Fruit.FeiraOnline.entities.Order;
import Fair.Fruit.FeiraOnline.entities.OrderItem;
import Fair.Fruit.FeiraOnline.entities.Product;
import Fair.Fruit.FeiraOnline.entities.enums.OrderStatus;
import Fair.Fruit.FeiraOnline.exception.BusinessRuleException;
import Fair.Fruit.FeiraOnline.exception.OrderNotFoundException;
import Fair.Fruit.FeiraOnline.repository.Clients;
import Fair.Fruit.FeiraOnline.repository.OrderItems;
import Fair.Fruit.FeiraOnline.repository.Orders;
import Fair.Fruit.FeiraOnline.repository.Products;
import Fair.Fruit.FeiraOnline.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final Orders repository;
    private final Clients clientsRepository;
    private final Products productsRepository;
    private final OrderItems orderItemsRepository;

    @Override
    @Transactional
    public Order save(OrderDTO dto) {
        Integer idClient = dto.getClient();
        Client client = clientsRepository
                .findById(idClient)
                .orElseThrow(() -> new BusinessRuleException("Client code not found."));

        Order order = new Order();
        order.setTotal(dto.getTotal());
        order.setOrderData(LocalDate.now());
        order.setClient(client);
        order.setStatus(OrderStatus.Realized);

        List<OrderItem> orderItems = convertItems(order, dto.getItems());
        repository.save(order);
        orderItemsRepository.saveAll(orderItems);
        order.setItems(orderItems);
        return order;
    }

    @Override
    public Optional<Order> getCompleteOrder(Integer id) {
        return repository.findByIdFetchItems(id);
    }

    @Override
    @Transactional
    public void StatusUpdate(Integer id, OrderStatus orderStatus) {
        repository
                .findById(id)
                .map( order -> {
                    order.setStatus(orderStatus);
                    return repository.save(order);
                }).orElseThrow(()-> new OrderNotFoundException());
    }

    private List<OrderItem> convertItems(Order order, List<OrderItemDTO> items){
        if(items.isEmpty()){
            throw new BusinessRuleException("it's not possible to order without products.");
        }

        return items
                .stream()
                .map(dto ->{
                    Integer idProduct = dto.getProduct();
                    Product product = productsRepository
                            .findById(idProduct)
                            .orElseThrow(() -> new BusinessRuleException("Invalid product code ."+ idProduct
                            ));

                    OrderItem orderItem = new OrderItem();
                    orderItem.setQuantity(dto.getQuantity());
                    orderItem.setOrder(order);
                    orderItem.setProduct(product);
                    return orderItem;
                }).collect(Collectors.toList());

    }

}
