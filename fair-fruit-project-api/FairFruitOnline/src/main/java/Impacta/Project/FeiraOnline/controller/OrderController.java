package Impacta.Project.FeiraOnline.controller;

import Impacta.Project.FeiraOnline.dto.OderDetailsItemDTO;
import Impacta.Project.FeiraOnline.dto.OrderDTO;
import Impacta.Project.FeiraOnline.dto.OrderDetailsDTO;
import Impacta.Project.FeiraOnline.dto.OrderStatusUpdateDTO;
import Impacta.Project.FeiraOnline.entities.Order;
import Impacta.Project.FeiraOnline.entities.OrderItem;
import Impacta.Project.FeiraOnline.entities.enums.OrderStatus;
import Impacta.Project.FeiraOnline.exception.OrderNotFoundException;
import Impacta.Project.FeiraOnline.exception.UserNotFoundException;
import Impacta.Project.FeiraOnline.service.OrderService;
import Impacta.Project.FeiraOnline.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

    private OrderService service;
    private UserServiceImpl userService;

    @PostMapping
    @ResponseStatus(CREATED)
    public Integer save(@RequestBody @Valid OrderDTO dto){
        Order order = service.save(dto);
        return order.getId();
    }

    @GetMapping("{id}")
    public OrderDetailsDTO getById(@PathVariable Integer id){
        return service
                .getCompleteOrder(id)
                .map(this::converter)
                .orElseThrow(OrderNotFoundException::new);
    }

    @PatchMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateStatus( @PathVariable Integer id,
                              @RequestBody OrderStatusUpdateDTO dto ){
        String newStatus = dto.getNewStatus();
        service.StatusUpdate(id, OrderStatus.valueOf(newStatus));
    }

    @GetMapping("/user/{id}")
    public List<OrderDetailsDTO> getCompleteOrdersByUserId(@PathVariable Integer id) {
        userService.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        List<Order> completeOrders = service.getAllCompleteOrder(id);
        if (CollectionUtils.isEmpty(completeOrders)) {
            throw new OrderNotFoundException();
        }
        return completeOrders.stream()
                .map(this::converter)
                .collect(Collectors.toList());
    }

    private OrderDetailsDTO converter(Order order){
        List<OderDetailsItemDTO> itemDTOs = converter(order.getItems());

        BigDecimal productTotal = itemDTOs.stream()
                .map(OderDetailsItemDTO::getProductTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal fees = order.getTotal().subtract(productTotal);

        return OrderDetailsDTO
                .builder()
                .code(order.getId())
                .orderData(order.getOrderData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(order.getUser().getCpf())
                .clientName(order.getUser().getName())
                .orderFees(fees)
                .total(order.getTotal())
                .payment(order.getPayment())
                .status(order.getStatus().name())
                .products(converter(order.getItems()))
                .build();
    }

    private List<OderDetailsItemDTO> converter(List<OrderItem> items) {
        if (CollectionUtils.isEmpty(items)) {
            return Collections.emptyList();
        }
        return items.stream().map( item -> OderDetailsItemDTO
                .builder()
                .productName(item.getProduct().getName())
                .productImage(item.getProduct().getImage())
                .productPrice(item.getProduct().getPrice())
                .productQuantity(item.getQuantity())
                .productTotal(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .build()
        ).collect(Collectors.toList());
    }
}
