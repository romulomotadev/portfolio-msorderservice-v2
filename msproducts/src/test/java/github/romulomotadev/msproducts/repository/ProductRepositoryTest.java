package github.romulomotadev.msproducts.repository;

import github.romulomotadev.msproducts.entities.Category;
import github.romulomotadev.msproducts.entities.Product;
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
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setName("Electronics");
        category = categoryRepository.save(category);

        product = new Product();
        product.setName("Smartphone XYZ");
        product.setDescription("A great phone");
        product.setSku("SKU123");
        product.setPrice(999.99);
        product.setActive(true);
        product.setCategory(category);
        product = productRepository.save(product);
    }

    @Test
    void searchProductByName_ShouldReturnProducts_WhenProductExists() {
        Page<Product> result = productRepository.searchProductByName("Smartphone", PageRequest.of(0, 10));

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Smartphone XYZ");
        assertThat(result.getContent().get(0).getActive()).isTrue();
    }

    @Test
    void searchProductByName_CaseInsensitiveSearch() {
        Page<Product> result = productRepository.searchProductByName("xyz", PageRequest.of(0, 10));

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Smartphone XYZ");
    }

    @Test
    void findProductsByCategoryName_ShouldReturnProducts() {
        Page<Product> result = productRepository.findProductsByCategoryName("Electronics", PageRequest.of(0, 10));

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Smartphone XYZ");
    }

    @Test
    void findBySku_ShouldReturnProduct() {
        Product result = productRepository.findBySku("SKU123");

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Smartphone XYZ");
    }
}
