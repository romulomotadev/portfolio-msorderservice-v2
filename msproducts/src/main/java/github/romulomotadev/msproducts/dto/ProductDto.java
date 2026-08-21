package github.romulomotadev.msproducts.dto;


import github.romulomotadev.msproducts.entities.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private String description;
    private String sku;
    private Double price;
    private Boolean active;

    private StockDto stockDto;


    public ProductDto(Product entity) {
        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        sku = entity.getSku();
        price = entity.getPrice();
        active = entity.getActive();

        stockDto = new StockDto(entity);
    }
}
