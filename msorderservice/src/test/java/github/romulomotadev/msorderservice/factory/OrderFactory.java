package github.romulomotadev.msorderservice.factory;

import github.romulomotadev.msorderservice.entities.Order;
import github.romulomotadev.msorderservice.entities.OrderItem;
import github.romulomotadev.msorderservice.entities.RequestStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderFactory {

    public static Order createOrder(){

        Order order = new Order();
        order.setId(1L);
        order.setClientName("Cliente 1");
        order.setClientDocument("123456789");
        order.setCreationDate(LocalDateTime.now());
        order.setTotalValue(10.00);
        order.setRequestStatus(RequestStatus.CREATED);

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(OrderItemFactory.createdOrderItem());
        order.setOrderItems(orderItems);

        return order;
    }
}
