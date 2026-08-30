package github.romulomotadev.msproducts.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import github.romulomotadev.msproducts.entities.Product;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder( { "id", "sku", "name", "category", "description", "price", "active"})
public class ProductMinDto {

    private Long id;
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Description is required")
    private String description;
    @NotBlank(message = "SKU is required")
    private String sku;
    @Positive(message = "Price must be positive")
    private Double price;
    @NotNull(message = "Active is required")
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
