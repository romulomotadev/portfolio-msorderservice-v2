package github.romulomotadev.msorderservice.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import github.romulomotadev.msorderservice.entities.OrderItem;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder({"id", "productName", "quantity", "unitPrice"})
public class OrderItemDto {

    private Long id;
    private String productName;
    private Integer quantity;
    @JsonProperty("unitPrice")
    @JsonAlias({"uniPrice", "unitPrice", "price"})
    private Double unitPrice;


    public OrderItemDto(OrderItem entity) {
        this.id = entity.getId();
        this.productName = entity.getProductName();
        this.quantity = entity.getQuantity();
        this.unitPrice = entity.getUniPrice();
    }

}
