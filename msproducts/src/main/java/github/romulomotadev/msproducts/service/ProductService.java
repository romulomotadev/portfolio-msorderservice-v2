package github.romulomotadev.msproducts.service;


import github.romulomotadev.msproducts.dto.ProductDto;
import github.romulomotadev.msproducts.dto.ProductMinDto;
import github.romulomotadev.msproducts.entities.Category;
import github.romulomotadev.msproducts.entities.Product;
import github.romulomotadev.msproducts.entities.Stock;
import github.romulomotadev.msproducts.exception.exceptions.exceptions.ResourceNotFoundException;
import github.romulomotadev.msproducts.repository.CategoryRepository;
import github.romulomotadev.msproducts.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    //============ POST ===============//

    //SALVAR PRODUTO/CATEGORY -> STOCK INICIADO ZERO
    @Transactional
    public ProductDto save(ProductMinDto productDto) {

        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setSku(productDto.getSku());
        product.setPrice(productDto.getPrice());
        product.setActive(productDto.getActive());

        Category category = categoryRepository.findById(productDto.getCategoryId()).orElseThrow(
                ()-> new ResourceNotFoundException("not found category information with product: "
                        + productDto.getCategoryId()));
        product.setCategory(category);

        Stock stock = new Stock();
        stock.setQuantity(0);
        stock.setMinQuantity(0);
        stock.setProduct(product);
        product.setStock(stock);

        productRepository.save(product);

        return new ProductDto(product);
    }
}
