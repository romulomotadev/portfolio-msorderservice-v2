package github.romulomotadev.msproducts.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

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


}
