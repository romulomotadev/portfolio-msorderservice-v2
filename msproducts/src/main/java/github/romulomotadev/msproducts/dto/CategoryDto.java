package github.romulomotadev.msproducts.dto;

import github.romulomotadev.msproducts.entities.Category;
import github.romulomotadev.msproducts.entities.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CategoryDto {

    private Long id;
    private String name;

    private List<ProductDto> productDtoList = new ArrayList<>();


    public CategoryDto(Category entity) {
        id = entity.getId();
        name = entity.getName();

        for (Product product : entity.getProducts()) {
            productDtoList.add(new ProductDto(product));
        }
    }
}
