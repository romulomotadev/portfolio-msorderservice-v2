package github.romulomotadev.msproducts.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import github.romulomotadev.msproducts.entities.Product;
import github.romulomotadev.msproducts.entities.Stock;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder({"id", "quantity", "minQuantity"})
public class StockDto {

    private Long id;
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
    @Positive(message = "Minimum quantity must be positive")
    private Integer minQuantity;


    public StockDto(Stock entity) {
        id = entity.getId();
        quantity = entity.getQuantity();
        minQuantity = entity.getMinQuantity();
    }
}
