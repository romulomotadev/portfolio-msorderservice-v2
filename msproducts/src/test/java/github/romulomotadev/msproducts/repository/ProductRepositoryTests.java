package github.romulomotadev.msproducts.repository;

import github.romulomotadev.msproducts.entities.Category;
import github.romulomotadev.msproducts.entities.Product;
import github.romulomotadev.msproducts.factory.ProductFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@DataJpaTest
public class ProductRepositoryTests {

    //================== DEPENDÊNCIAS ==================

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;

    //================== ATRIBUTOS ==================

    private Product product;
    private Pageable  pageable;

    //================== INICIALIZAÇÃO ==================

    @BeforeEach
    void setUp() {

        // FACTORY
        product = ProductFactory.createdProduct();

        product.setId(null);
        product.getCategory().setId(null);
        product.getStock().setId(null);

        // PERSISTE A CATEGORIA
        Category savedCategory =
                categoryRepository.save(product.getCategory());

        // ASSOCIA A CATEGORIA PERSISTIDA AO PRODUTO
        product.setCategory(savedCategory);

        // PERSISTE O PRODUTO
        product = productRepository.save(product);

        // PAGINAÇÃO
        pageable = PageRequest.of(0, 10);
    }

    //================== GET ==================

    // BUSCA PRODUTOS POR NOME EXISTENTE
    @Test
    @DisplayName("Busca produto por nome deve retornar Page quando nome existir")
    void searchProductByNameShouldReturnPageWhenNameExists(){

        //PREPARAR
        String productName = product.getName();

        //EXECUTAR
        Page<Product> result = productRepository.searchProductByName(productName, pageable);

        //VALIDAÇÃO
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(result.getContent().getFirst().getName(), product.getName());
    }

    // BUSCA PRODUTOS POR NOME INEXISTENTE
    @Test
    @DisplayName("Busca produto por nome deve retornar Page vazia nome quando nome não existir")
    void searchProductByNameShouldReturnEmptyPageWhenNameDoesNotExist(){

        //PREPARAR
        String productName = "produtoInexistente";

        //EXECUTAR
        Page<Product> result = productRepository.searchProductByName(productName, pageable);

        //VALIDAÇÃO
        Assertions.assertTrue(result.isEmpty());
    }

    // BUSCA PRODUTOS POR NOME IGNORANDO MINUSCULAS E MAIÚSCULAS
    @Test
    @DisplayName("Busca produto por nome ignorando maiúsculas e minusculas")
    void searchProductByNameShouldIgnoreCase(){

        //PREPARAR
        String productName = "PrOdUcT 1";

        //EXECUTAR
        Page<Product> result = productRepository.searchProductByName(productName, pageable);

        //VALIDAÇÃO
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(result.getContent().getFirst().getId(), product.getId());
        Assertions.assertEquals(result.getContent().getFirst().getName(), product.getName());
    }

    // BUSCA PRODUTOS POR NOME IGNORANDO MINUSCULAS E MAIÚSCULAS
    @Test
    @DisplayName("Busca todos produto por status e retorna produto ativos")
    void findAllProductsStatusShouldReturnActiveProducts(){

        //PREPARAR
        boolean status = true;

        //EXECUTAR
        Page<Product> result = productRepository.findAllProductsStatus(status, pageable);

        //VALIDAÇÃO
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(result.getContent().getFirst().getId(), product.getId());
        Assertions.assertEquals(result.getContent().getFirst().getName(), product.getName());
    }

    // BUSCA PRODUTOS POR NOME EXISTENTE
    @Test
    @DisplayName("Busca todos produto por categoria deve retornar produtos da categoria")
    void findProductsByCategoryNameShouldReturnProductsFromCategory(){

        //PREPARAR
        String categoryName = product.getCategory().getName();

        //EXECUTAR
        Page<Product> result = productRepository.findProductsByCategoryName(categoryName, pageable);

        //VALIDAÇÃO
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(result.getContent().getFirst().getId(), product.getId());
        Assertions.assertEquals(result.getContent().getFirst().getName(), product.getName());
        Assertions.assertEquals(result.getContent().getFirst().getCategory().getId(), product.getCategory().getId());
        Assertions.assertEquals(result.getContent().getFirst().getCategory().getName(), product.getCategory().getName());
    }

}
