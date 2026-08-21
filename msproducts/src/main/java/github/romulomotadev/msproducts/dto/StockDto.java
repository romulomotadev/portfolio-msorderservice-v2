package github.romulomotadev.msproducts.dto;

import github.romulomotadev.msproducts.entities.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StockDto {

    private Long id;
    private Integer quantity;
    private Integer minQuantity;


    public StockDto(Product entity) {
        id = entity.getId();
        quantity = entity.getStock().getQuantity();
        minQuantity = entity.getStock().getMinQuantity();
    }
}
