package github.romulomotadev.msproducts.controller;

import github.romulomotadev.msproducts.dto.CategoryDto;
import github.romulomotadev.msproducts.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    //============ POST ===============//

    @PostMapping
    public ResponseEntity<CategoryDto> save(@RequestBody CategoryDto categoryDto) {
        CategoryDto dto = categoryService.save(categoryDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }


    //============ GET ===============//

    //BUSCA POR ID
    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDto> findById(@PathVariable Long id){
        CategoryDto dto = categoryService.findById(id);
        return ResponseEntity.ok(dto);
    }

    //BUSCA POR NOME
    @GetMapping(value = "/search")
    public ResponseEntity<List<CategoryDto>> searchByName(@RequestParam String name){
        List<CategoryDto> dto = categoryService.searchByName(name);
        return ResponseEntity.ok(dto);
    }

    //BUSCAR TODAS CATEGORIAS
    @GetMapping
    public ResponseEntity<List<CategoryDto>> findAll(){
        List<CategoryDto> dto = categoryService.findAll();
        return ResponseEntity.ok(dto);
    }


    //============ PUT ===============//

    @PutMapping(value = "/{id}")
    public ResponseEntity<CategoryDto> update(@PathVariable Long id, @RequestBody CategoryDto categoryDto){
        CategoryDto dto = categoryService.update(id, categoryDto);
        return ResponseEntity.ok(dto);
    }


    //============ DELETE ===============//

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
