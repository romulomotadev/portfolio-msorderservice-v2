package github.romulomotadev.msproducts.controller;

import github.romulomotadev.msproducts.dto.CategoryDto;
import github.romulomotadev.msproducts.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    //============ POST ===============//

    @PostMapping
    public CategoryDto save(@RequestBody CategoryDto categoryDto) {
        return categoryService.save(categoryDto);
    }


    //============ GET ===============//

    //Busca por ID
    @GetMapping(value = "/{id}")
    public CategoryDto findById(@PathVariable Long id){
        return categoryService.findById(id);
    }

    //Busca por nome
    @GetMapping(value = "/search")
    public List<CategoryDto> searchByName(@RequestParam String name){
        return categoryService.searchByName(name);
    }

}
