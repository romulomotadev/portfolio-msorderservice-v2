package github.romulomotadev.msproducts.service;

import github.romulomotadev.msproducts.dto.ProductDto;
import github.romulomotadev.msproducts.dto.ProductMinDto;
import github.romulomotadev.msproducts.entities.Category;
import github.romulomotadev.msproducts.entities.Product;
import github.romulomotadev.msproducts.exception.exceptions.ResourceNotFoundException;
import github.romulomotadev.msproducts.repository.CategoryRepository;
import github.romulomotadev.msproducts.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static github.romulomotadev.msproducts.factory.CategoryFactory.createdCategory;
import static github.romulomotadev.msproducts.factory.ProductFactory.createdProduct;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;


    // ========= DATA =========

    private Long existingId;
    private Long nonExistingId;

    private String existingSku;
    private String nonExistingSku;

    private String existingCategoryName;
    private String nonExistingCategoryName;

    private String existingProductName;
    private String nonExistingProductName;

    private boolean active;

    private PageImpl<Product> page;
    private PageImpl<Product> pageEmpty;

    private Product product;
    private Category category;

    private ProductMinDto productDto;


    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 2L;

        existingSku = "SKU-EXIST";
        nonExistingSku = "SKU-NON-EXIST";

        existingCategoryName = "EXIST-CATEGORY";
        nonExistingCategoryName = "NON-EXIST-CATEGORY";

        existingProductName = "EXIST-PRODUCT";
        nonExistingProductName = "NON-EXIST-PRODUCT";

        active = true;

        product = createdProduct();
        category = createdCategory();

        productDto = new ProductMinDto(product);

        page = new PageImpl<>(List.of(product));
        pageEmpty = new PageImpl<>(List.of());
    }


    //============ SAVE ===============//

    //SALVAR PRODUTO/CATEGORIA
    @Test
    @DisplayName("save deve salvar e retornar ProductDTO")
    void saveShouldSaveAndReturnProductDto() {

        // PREPARA
        when(categoryRepository.findById(productDto.getCategoryId()))
                .thenReturn(Optional.of(product.getCategory()));

        when(productRepository.save(any(Product.class))).thenReturn(product);

        // EXECUTA
        ProductDto result = productService.save(productDto);

        // VERIFICA
        assertNotNull(result);
        assertEquals(product.getId(), result.getId());
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getCategory().getName(), result.getCategory().getName());
        assertEquals(product.getCategory().getId(), result.getCategory().getId());
        assertEquals(product.getCategory().getName(), result.getCategory().getName());

        verify(productRepository).save(any(Product.class));
        verifyNoMoreInteractions(productRepository);
    }


    //NAO SALVA QUANDO CATEGORIA NAO EXISTE
    @Test
    @DisplayName("save deve lançar ResourceNotFoundException quando a categoria não existe")
    void saveShouldThrowResourceNotFoundExceptionWhenCategoryNotFound() {

        // PREPARA
        // simular categoria ausente
        productDto.setCategoryId(nonExistingId);
        when(categoryRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // EXECUTA + VERIFICA
        assertThrows(ResourceNotFoundException.class,
                () -> productService.save(productDto));
    }


    //============ UPDATE ===============//

    // ID EXISTENTE
    @Test
    @DisplayName("update deve atualizar e retornar Product DTO quando ID existir")
    void updateShouldUpdateAndReturnProductDTOWhenIdExists() {

        // PREPARA
        when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(existingId)).thenReturn(Optional.of(category));
        when(productRepository.save(any())).thenReturn(product);

        // EXECUTA
        ProductDto result = productService.update(existingId, productDto);

        // VERIFICA
        assertEquals(existingId, result.getId());
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getCategory().getName(), result.getCategory().getName());
        assertEquals(product.getCategory().getId(), result.getCategory().getId());
        assertEquals(product.getCategory().getName(), result.getCategory().getName());

        verify(productRepository).findById(existingId);
        verify(productRepository).save(any());
        verifyNoMoreInteractions(productRepository);
    }

    // ID NAO EXISTENTE
    @Test
    @DisplayName("update deve lançar exceção quando ID não existir")
    void update_ShouldThrowException_WhenIdDoesNotExist() {

        // PREPARA
        when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // EXECUTA + VERIFICA
        assertThrows(ResourceNotFoundException.class, () ->
            productService.update(nonExistingId, productDto));
    }

    //SAVE CATEGORIA NAO EXISTENTE
    @Test
    @DisplayName("update deve lançar ResourceNotFoundException quando a categoria não existe")
    void updateShouldThrowResourceNotFoundExceptionWhenCategoryNotFound() {

        // PREPARA
        // simular categoria ausente
        productDto.setCategoryId(nonExistingId);

        when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // EXECUTA + VERIFICA
        assertThrows(ResourceNotFoundException.class,
                () -> productService.update(existingId, productDto));
    }


    //============ GET ===============//

    // ID EXISTENTE
    @Test
    @DisplayName("findById deve retornar DTO quando ID existir")
    void findById_ShouldReturnDTO_WhenIdExists() {

        // PREPARA
        when(productRepository.findById(existingId)).thenReturn(Optional.of(product));

        // EXECUTA
        ProductDto result = productService.findById(existingId);

        // VERIFICA
        assertNotNull(result);
        assertEquals(existingId, result.getId());

        verify(productRepository).findById(existingId);
        verifyNoMoreInteractions(productRepository);
    }

    // ID NAO EXISTENTE
    @Test
    @DisplayName("findById deve lançar exceção quando ID não existir")
    void findById_ShouldThrowException_WhenIdDoesNotExist() {

        // PREPARA
        when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // EXECUTA + VERIFICA
        assertThrows(ResourceNotFoundException.class, () ->
            productService.findById(nonExistingId));
    }

    // CODIGO SKU EXISTENTE
    @Test
    @DisplayName("findBySku deve retornar Product DTO quando SKU existir")
    void findBySkuShouldReturnProductDtoWhenSkuExists() {

        // PREPARA
        when(productRepository.existsBySku(existingSku)).thenReturn(true);
        when(productRepository.findBySku(existingSku)).thenReturn(product);

        // EXECUTA
        ProductDto result = productService.findBySku(existingSku);

        // VERIFICA
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getCategory().getName(), result.getCategory().getName());
        assertEquals(product.getCategory().getId(), result.getCategory().getId());
        assertEquals(product.getCategory().getName(), result.getCategory().getName());
    }

    // SKU NAO EXISTENTE
    @Test
    @DisplayName("findBySku deve lançar Resource Not Found Exception quando sku não existir")
    void findBySkuShouldThrowResourceNotFoundExceptionWhenSkuNotExistis(){

        // PREPARA
        when(productRepository.existsBySku(nonExistingSku)).thenReturn(false);

        // EXECUTA + VERIFICA
        assertThrows(ResourceNotFoundException.class,
                () -> productService.findBySku(nonExistingSku));
    }

    // BUSCA TODOS PRODUTOS
    @Test
    @DisplayName("findAll deve retornar page de Products DTO")
    void findAllShouldReturnPageProductsDTO(){

        // PREPARA
        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);
        Pageable pageable = PageRequest.of(0, 10);

        // EXECUTA
        Page<ProductDto> result = productService.findAll(pageable);

        // VERIFICA
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getSize());
        assertEquals(0, result.getNumber());
        assertEquals(1, result.getNumberOfElements());

        assertEquals(result.iterator().next().getName(), product.getName());
        assertEquals(result.iterator().next().getSku(), product.getSku());
        assertEquals(result.iterator().next().getCategory().getId(), product.getCategory().getId());
        assertEquals(result.iterator().next().getCategory().getName(), product.getCategory().getName());
    }

    // BUSCA TODOS PRODUTOS QUANDO NÃO HOUVER PRODUTOS
    @Test
    @DisplayName("findAll deve retornar page vazia quando não houver dados")
    void findAllShouldReturnEmptyPageWhenNoData(){

        // PREPARA
        when(productRepository.findAll(any(Pageable.class))).thenReturn(pageEmpty);
        Pageable pageable = PageRequest.of(0, 10);

        // EXECUTA
        Page<ProductDto> result = productService.findAll(pageable);

        // VERIFICA
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(0, result.getSize());
        assertEquals(0, result.getNumber());
        assertEquals(0, result.getNumberOfElements());
    }

    // BUSCA TODOS PRODUTOS POR CATEGORIA
    @Test
    @DisplayName("findProductsByCategoryName deve retornar Page de Produtos DTO quando categoria existir")
    void findProductsByCategoryNameShouldReturnPageProductsDtoWhenCategoryExisting(){

        //PREPARA
        when(productRepository.findProductsByCategoryName(anyString(), any(Pageable.class))).thenReturn(page);
        Pageable pageable = PageRequest.of(0, 10);

        //EXECUTA
        Page<ProductDto> result = productService.findProductsByCategoryName(existingCategoryName, pageable);

        //VERIFICA
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getSize());
        assertEquals(0, result.getNumber());
        assertEquals(1, result.getNumberOfElements());

        assertEquals(result.iterator().next().getName(), product.getName());
        assertEquals(result.iterator().next().getSku(), product.getSku());
        assertEquals(result.iterator().next().getCategory().getId(), product.getCategory().getId());
        assertEquals(result.iterator().next().getCategory().getName(), product.getCategory().getName());
    }

    //BUSCA TODOS PRODUTOS POR CATEGORIA, QUANDO CATEGORIA INEXISTENTE
    @Test
    @DisplayName("findProductsByCategoryName deve retornar page vazia quando não houver dados")
    void findProductsByCategoryNameAllShouldReturnEmptyPageWhenNoData(){

        // PREPARA
        when(productRepository.findProductsByCategoryName(anyString(), any(Pageable.class))).thenReturn(pageEmpty);
        Pageable pageable = PageRequest.of(0, 10);

        // EXECUTA
        Page<ProductDto> result = productService.findProductsByCategoryName(nonExistingCategoryName,pageable);

        // VERIFICA
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(0, result.getSize());
        assertEquals(0, result.getNumber());
        assertEquals(0, result.getNumberOfElements());
    }

    //BUSCA PRODUTOS POR NOME EXISTENTE
    @Test
    @DisplayName("searchProductByName deve retornar Page Product Dto quando nome produto existir")
    void searchProductByNameShouldReturnPageProductDtoWhenNameExist(){

        // PREPARA
        when(productRepository.searchProductByName(anyString(), any(Pageable.class))).thenReturn(page);
        Pageable pageable = PageRequest.of(0, 10);

        // EXECUTA
        Page<ProductDto> result = productService.searchProductByName(existingProductName ,pageable);

        // VERIFICA
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getSize());
        assertEquals(0, result.getNumber());
        assertEquals(1, result.getNumberOfElements());

        assertEquals(result.iterator().next().getName(), product.getName());
        assertEquals(result.iterator().next().getSku(), product.getSku());
        assertEquals(result.iterator().next().getCategory().getId(), product.getCategory().getId());
        assertEquals(result.iterator().next().getCategory().getName(), product.getCategory().getName());
    }

    //BUSCA TODOS PRODUTOS POR CATEGORIA, QUANDO CATEGORIA INEXISTENTE
    @Test
    @DisplayName("findProductsByCategoryName deve retornar page vazia quando nome nao existir")
    void findProductsByCategoryNameAllShouldReturnEmptyPageWhenNameNotExiting(){

        // PREPARA
        when(productRepository.searchProductByName(anyString(), any(Pageable.class))).thenReturn(pageEmpty);
        Pageable pageable = PageRequest.of(0, 10);

        // EXECUTA
        Page<ProductDto> result = productService.searchProductByName(nonExistingProductName,pageable);

        // VERIFICA
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(0, result.getSize());
        assertEquals(0, result.getNumber());
        assertEquals(0, result.getNumberOfElements());
    }

    //BUSCA TODOS PRODUTOS ATIVOS
    @Test
    @DisplayName("findAllProductsStatus deve retornar page produto DTO ativos")
    void findAllProductsStatusShouldRetornPageProdutosDtoActive(){

        //PREPARAR
        when(productRepository.findAllProductsStatus(anyBoolean(), any(Pageable.class))).thenReturn(page);
        Pageable pageable = PageRequest.of(0, 10);

        // EXECUTA
        Page<ProductDto> result = productService.findAllProductsStatus(active, pageable);

        // VERIFICAR
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getSize());
        assertEquals(0, result.getNumber());
        assertEquals(1, result.getNumberOfElements());

        assertEquals(result.iterator().next().getActive(), product.getActive());
        assertEquals(result.iterator().next().getName(), product.getName());
        assertEquals(result.iterator().next().getSku(), product.getSku());
        assertEquals(result.iterator().next().getCategory().getId(), product.getCategory().getId());
        assertEquals(result.iterator().next().getCategory().getName(), product.getCategory().getName());
    }


    //============ DELETE ===============//

    // ID EXISTE
    @Test
    @DisplayName("delete deve remover produto quando ID existir")
    void deleteShouldRemoveProductWhenIdExists() {

        // ARRANGE
        when(productRepository.existsById(existingId)).thenReturn(true);

        // ACT
        productService.deleteProduct(existingId);

        // ASSERT
        verify(productRepository).existsById(existingId);
        verify(productRepository).deleteById(existingId);
        verifyNoMoreInteractions(productRepository);
    }

    // ID NAO EXISTE
    @Test
    @DisplayName("delete deve lançar exceção Resource Not Found Exception quando ID não existir")
    void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        // ARRANGE
        when(productRepository.existsById(nonExistingId)).thenReturn(false);

        // ACT + ASSERT
        assertThrows(ResourceNotFoundException.class, () ->
                productService.deleteProduct(nonExistingId));
    }


}
