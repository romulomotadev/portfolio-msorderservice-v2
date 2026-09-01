package github.romulomotadev.msproducts.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import github.romulomotadev.msproducts.entities.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@JsonPropertyOrder({"id", "name"})
public class CategoryDto {

    private Long id;
    @NotBlank(message = "Name is required")
    private String name;


    public CategoryDto(Category entity) {
        id = entity.getId();
        name = entity.getName();
    }
}
