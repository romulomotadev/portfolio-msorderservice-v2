package github.romulomotadev.msproducts.dto;

import github.romulomotadev.msproducts.entities.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.net.ssl.SSLSession;

@Data
@NoArgsConstructor
public class ProductMinDto {

    private Long id;
    private String name;
    private String description;
    private String sku;
    private Double price;
    private Boolean active;
    private Long categoryId;

    public ProductMinDto(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.sku = entity.getSku();
        this.price = entity.getPrice();
        this.active = entity.getActive();
        this.categoryId = entity.getCategory().getId();
    }
}
