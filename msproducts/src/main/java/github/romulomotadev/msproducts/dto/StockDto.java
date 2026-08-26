package github.romulomotadev.msproducts.dto;

import github.romulomotadev.msproducts.entities.Product;
import github.romulomotadev.msproducts.entities.Stock;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StockDto {

    private Long id;
    private Integer quantity;
    private Integer minQuantity;


    public StockDto(Stock entity) {
        id = entity.getId();
        quantity = entity.getQuantity();
        minQuantity = entity.getMinQuantity();
    }
}
