package github.romulomotadev.msproducts.controller;

import github.romulomotadev.msproducts.dto.ProductDto;
import github.romulomotadev.msproducts.dto.ProductMinDto;
import github.romulomotadev.msproducts.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    //============ POST ===============//

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductMinDto productDto){
        ProductDto dto = productService.save(productDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    //============ PUT ===============//

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @RequestBody ProductMinDto dto){
        ProductDto dtoUpdated = productService.update(id, dto);
        return ResponseEntity.ok().body(dtoUpdated);
    }

    //============ GET ===============//

    // BUSCA PRODUTO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable Long id){
        ProductDto dto = productService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    // BUSCA PRODUTO PELO CODIGO SKU
    @GetMapping("/sku")
    public ResponseEntity<ProductDto> findBySku(@RequestParam String sku){
        ProductDto dto = productService.findBySku(sku);
        return ResponseEntity.ok().body(dto);
    }

    // BUSCA TODOS OS PRODUTOS
    @GetMapping
    public ResponseEntity<Page<ProductDto>> findAll(Pageable pageable){
        Page<ProductDto> dto = productService.findAll(pageable);
        return ResponseEntity.ok().body(dto);
    }

    // BUSCA TODOS PRODUTOS POR CATEGORIA
    @GetMapping("/category")
    public ResponseEntity<Page<ProductDto>> findProductsByCategoryName(@RequestParam String category, Pageable pageable){
        Page<ProductDto> dto = productService.findProductsByCategoryName(category, pageable);
        return ResponseEntity.ok().body(dto);
    }

    // ENCONTRAR PRODUTOS POR NOME
    @GetMapping("/search")
    public ResponseEntity<Page<ProductDto>> searchProductByName(@RequestParam String name, Pageable pageable){
        Page<ProductDto> dto = productService.searchProductByName(name, pageable);
        return ResponseEntity.ok().body(dto);
    }

    // BUSCA TODOS PRODUTOS ATIVOS OU INATIVOS
    @GetMapping("/status")
    public ResponseEntity<Page<ProductDto>> findAllProductsStatus(@RequestParam Boolean active, Pageable pageable){
        Page<ProductDto> dto = productService.findAllProductsStatus(active, pageable);
        return ResponseEntity.ok().body(dto);
    }


    //============ DELETE ===============//

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
