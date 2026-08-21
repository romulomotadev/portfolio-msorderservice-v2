package github.romulomotadev.msproducts.dto;


import github.romulomotadev.msproducts.entities.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

@Data
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private String description;
    private String sku;
    private Double price;
    private Boolean active;

    private CategoryDto categoryDto;

    private StockDto stockDto;


    public ProductDto(Product entity) {
        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        sku = entity.getSku();
        price = entity.getPrice();
        active = entity.getActive();

        categoryDto = new CategoryDto(entity.getCategory());
        stockDto = new StockDto(entity);
    }
}
