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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        copyDtoToEntity(productDto, product);

        Stock stock = new Stock();
        stock.setQuantity(0);
        stock.setMinQuantity(0);
        stock.setProduct(product);
        product.setStock(stock);

        productRepository.save(product);

        return new ProductDto(product);
    }


    //============ PUT ===============//

    @Transactional
    public ProductDto update(Long id, ProductMinDto dto) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("not found product information with id: " + id));
        copyDtoToEntity(dto, product);

        return new ProductDto(product);
    }


    //============ GET ===============//

    // BUSCA PRODUTO POR ID
    @Transactional(readOnly = true)
    public ProductDto findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("not found product information with id: " + id));
        return new ProductDto(product);
    }

    // BUSCA PRODUTO PELO CODIGO SKU
    @Transactional(readOnly = true)
    public ProductDto findBySku(String sku) {
        if (sku == null || sku.isEmpty())
            throw new ResourceNotFoundException("not found product information with sku: " + sku);
        Product product = productRepository.findBySku(sku);
        return new ProductDto(product);
    }

    // BUSCA TODOS OS PRODUTOS
    public Page<ProductDto> findAll(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(ProductDto::new);
    }

    // BUSCA TODOS PRODUTOS POR CATEGORIA
    @Transactional(readOnly = true)
    public Page<ProductDto> findProductsByCategoryName(String categoryName, Pageable pageable) {
        Page<Product> products = productRepository.findProductsByCategoryName(categoryName, pageable);
        return products.map(ProductDto::new);
    }

    // ENCONTRAR PRODUTOS POR NOME
    @Transactional(readOnly = true)
    public Page<ProductDto> searchProductByName(String name, Pageable pageable) {
        Page<Product> products = productRepository.searchProductByName(name, pageable);
        return products.map(ProductDto::new);
    }

    // BUSCA TODOS PRODUTOS ATIVOS OU INATIVOS
    @Transactional(readOnly = true)
    public Page<ProductDto> findAllProductsStatus(Boolean active, Pageable pageable) {
        Page<Product> products = productRepository.findAllProductsStatus(active, pageable);
        return products.map(ProductDto::new);
    }


    //============ DELETE ===============//

    @Transactional
    public void deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else throw new ResourceNotFoundException("not found product information with id: " + id);
    }


    //============ AUX ===============//

    private void copyDtoToEntity(ProductMinDto dto, Product product) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setSku(dto.getSku());
        product.setPrice(dto.getPrice());
        product.setActive(dto.getActive());

        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("not found category information with product: "
                        + dto.getCategoryId()));
        product.setCategory(category);
    }

}
