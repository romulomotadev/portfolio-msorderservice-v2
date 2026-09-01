package github.romulomotadev.msorderservice.dto;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder( { "id", "sku", "name", "category", "description", "price", "active"})
public class ProductResponseDto {

    private Long id;
    private String name;
    private String description;
    private String sku;
    private Double price;
    private Boolean active;

}
