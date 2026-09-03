package github.romulomotadev.msorderservice.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import github.romulomotadev.msorderservice.entities.Order;
import github.romulomotadev.msorderservice.entities.OrderItem;
import github.romulomotadev.msorderservice.entities.RequestStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@JsonPropertyOrder({"id", "requestStatus",  "creationDate", "clientName", "clientDocument",  "orderItem", "totalValue"})
public class OrderDto {

    private Long id;
    private String clientName;
    private String clientDocument;
    private LocalDateTime creationDate;

    private Double totalValue;

    private RequestStatus requestStatus;

    @JsonProperty("orderItems")
    @JsonAlias({"items", "orderItems", "orderItem"})
    private List<OrderItemDto> orderItem = new ArrayList<>();


    public OrderDto(Order entity) {
        this.id = entity.getId();
        this.clientName = entity.getClientName();
        this.clientDocument = entity.getClientDocument();
        this.creationDate = entity.getCreationDate();
        this.totalValue = entity.getTotalValue();

        this.requestStatus = entity.getRequestStatus();

        if (entity.getOrderItems() != null) {
            for (OrderItem item : entity.getOrderItems()) {
                this.orderItem.add(new OrderItemDto(item));
            }
        }
    }

}
