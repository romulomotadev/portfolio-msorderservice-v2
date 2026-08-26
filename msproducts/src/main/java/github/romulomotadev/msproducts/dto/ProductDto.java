package github.romulomotadev.msproducts.dto;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import github.romulomotadev.msproducts.entities.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder( { "id", "sku", "name", "category", "description", "price", "active"})
public class ProductDto {

    private Long id;
    private String name;
    private String description;
    private String sku;
    private Double price;
    private Boolean active;

    private CategoryDto category;


    public ProductDto(Product entity) {
        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        sku = entity.getSku();
        price = entity.getPrice();
        active = entity.getActive();

        category = new CategoryDto(entity.getCategory());
    }
}
