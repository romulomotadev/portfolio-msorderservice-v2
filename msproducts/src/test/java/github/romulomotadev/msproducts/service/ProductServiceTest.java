package github.romulomotadev.msproducts.service;

import github.romulomotadev.msproducts.dto.ProductDto;
import github.romulomotadev.msproducts.entities.Category;
import github.romulomotadev.msproducts.entities.Product;
import github.romulomotadev.msproducts.repository.CategoryRepository;
import github.romulomotadev.msproducts.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        Category category = new Category();
        category.setName("Electronics");
        category = categoryRepository.save(category);

        Product product = new Product();
        product.setName("Smartphone XYZ");
        product.setDescription("A great phone");
        product.setSku("SKU123");
        product.setPrice(999.99);
        product.setActive(true);
        product.setCategory(category);
        productRepository.save(product);
    }

    @Test
    void searchProductByName_ShouldReturnProductDtos() {
        Page<ProductDto> result = productService.searchProductByName("Smartphone", PageRequest.of(0, 10));

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Smartphone XYZ");
        assertThat(result.getContent().get(0).getSku()).isEqualTo("SKU123");
        assertThat(result.getContent().get(0).getActive()).isTrue();
    }
}
