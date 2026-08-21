package github.romulomotadev.msproducts.controller;

import github.romulomotadev.msproducts.dto.CategoryDto;
import github.romulomotadev.msproducts.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping(value = "/{id}")
    public CategoryDto findById(@PathVariable Long id){
        return categoryService.findById(id);
    }

}
