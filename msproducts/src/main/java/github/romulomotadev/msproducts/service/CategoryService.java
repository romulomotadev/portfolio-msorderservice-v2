package github.romulomotadev.msproducts.service;

import github.romulomotadev.msproducts.dto.CategoryDto;
import github.romulomotadev.msproducts.entities.Category;
import github.romulomotadev.msproducts.exception.exceptions.ResourseNotFoundException;
import github.romulomotadev.msproducts.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    //============ POST ===============//

    @Transactional
    public CategoryDto save(CategoryDto categoryDto) {

        Category category = new Category();
        category.setName(categoryDto.getName());

        categoryRepository.save(category);
        return new CategoryDto(category);
    }


    //============ GET ===============//

    // BUSCA POR ID
    @Transactional(readOnly = true)
    public CategoryDto findById(Long id) {
        return new CategoryDto(categoryRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("Category not found" + id)));
    }

    // BUSCA POR NOME
    @Transactional(readOnly = true)
    public List<CategoryDto> searchByName(String name) {
        List<Category> categories = categoryRepository.searchByName(name);
        return categories.stream().map(CategoryDto::new).toList();
    }
}
