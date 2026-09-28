package github.romulomotadev.msorderservice.factory;


import github.romulomotadev.msorderservice.entities.OrderItem;

public class OrderItemFactory {

    public static OrderItem createdOrderItem(){
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setQuantity(1);
        orderItem.setUniPrice(10.0);
        return orderItem;
    }
}
