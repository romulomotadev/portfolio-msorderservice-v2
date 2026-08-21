package github.romulomotadev.msproducts.service;


import github.romulomotadev.msproducts.dto.ProductDto;
import github.romulomotadev.msproducts.entities.Category;
import github.romulomotadev.msproducts.entities.Product;
import github.romulomotadev.msproducts.entities.Stock;
import github.romulomotadev.msproducts.exception.exceptions.ResourseNotFoundException;
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
    public ProductDto save(ProductDto productDto) {

        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setSku(productDto.getSku());
        product.setPrice(productDto.getPrice());
        product.setActive(productDto.getActive());

        Category category = categoryRepository.findById(productDto.getCategoryDto().getId()).orElseThrow(
                ()-> new ResourseNotFoundException("not found category information with product: "
                        + productDto.getCategoryDto().getId()));
        product.setCategory(category);

        Stock stock = new Stock();
        stock.setQuantity(0);
        stock.setMinQuantity(0);
        product.setStock(stock);

        productRepository.save(product);

        return new ProductDto(product);
    }


    //SALVAR STOCK
    @Transactional
    public ProductDto saveStock(ProductDto productDto, Long id) {

        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourseNotFoundException("not found product with id: " + id));

        Category category = product.getCategory();
        product.setCategory(category);

        Stock stock = product.getStock();
        stock.setQuantity(productDto.getStockDto().getQuantity());
        stock.setMinQuantity(productDto.getStockDto().getMinQuantity());
        product.setStock(stock);

        productRepository.save(product);

        return new ProductDto(product);
    }
}
