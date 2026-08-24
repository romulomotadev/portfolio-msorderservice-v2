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
        Category category = categoryRepository.findById(id).orElseThrow(
                ()-> new ResourseNotFoundException("Category not found"));
        return new CategoryDto(category);
    }

    // BUSCA POR NOME
    @Transactional(readOnly = true)
    public List<CategoryDto> searchByName(String name) {
        List<Category> categories = categoryRepository.searchByName(name);
        return categories.stream().map(CategoryDto::new).toList();
    }

    // BUSCA TODAS CATEGORIAS
    @Transactional(readOnly = true)
    public List<CategoryDto> findAll() {
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList.stream().map(CategoryDto::new).toList();
    }


    //============ PUT ===============//

    @Transactional
    public CategoryDto update(Long id, CategoryDto categoryDto){
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourseNotFoundException("Category not found"));
        category.setName(categoryDto.getName());
        categoryRepository.save(category);
        return new CategoryDto(category);
    }


    //============ DELETE ===============//

    @Transactional
    public void delete(Long id){
        if(!categoryRepository.existsById(id)){
            throw new ResourseNotFoundException("Category not found");
        }else{
            categoryRepository.deleteById(id);
        }
    }
}
