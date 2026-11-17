package github.romulomotadev.msproducts.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import github.romulomotadev.msproducts.config.KeycloakJwtConverter;
import github.romulomotadev.msproducts.config.SecurityConfig;
import github.romulomotadev.msproducts.dto.ProductDto;
import github.romulomotadev.msproducts.dto.ProductMinDto;
import github.romulomotadev.msproducts.entities.Product;
import github.romulomotadev.msproducts.exception.exceptions.ResourceNotFoundException;
import github.romulomotadev.msproducts.factory.ProductFactory;
import github.romulomotadev.msproducts.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import(SecurityConfig.class)
public class ProductControllerTests {

    //================== DEPENDÊNCIAS ==================

    @MockitoBean
    private ProductService productService;
    @MockitoBean
    private KeycloakJwtConverter keycloakJwtConverter;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    //================== ATRIBUTOS ==================

    private ProductDto productDto;
    private Long existingId;
    private Long nonExistingId;
    private String existingSku;
    private String nonExistingSku;
    private String category;
    private String productName;
    private Boolean active;
    private String jsonBody;
    private Page<ProductDto> page;


    //================== INICIALIZAÇÃO ==================

    @BeforeEach
    public void setup() throws JsonProcessingException {
        existingId = 1L;
        nonExistingId = 2L;
        existingSku = "sku1";
        nonExistingSku = "sku2";
        category = "category1";
        productName = "product1";
        active = true;

        //Factory
        Product product = ProductFactory.createdProduct();
        productDto = new ProductDto(product);
        ProductMinDto productMinDto = new ProductMinDto(product);

        //Corpo da requisição
        jsonBody = objectMapper.writeValueAsString(productMinDto);

        //Paginação
        Pageable pageable = PageRequest.of(0, 10);
        page = new PageImpl<>(List.of(productDto), pageable, 1);
    }

    //================== GET ==================

    //ID EXISTENTE
    @Test
    @DisplayName("Busca por ID deve retornar produto DTO quando ID existe")
    void findByIdShouldReturnProductDTOWhenIdExists() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(productService.findById(existingId)).thenReturn(productDto);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/products/{id}", existingId)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("PRODUCT_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JSON
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.name").value(productDto.getName()))
                .andExpect(jsonPath("$.category.name").value(productDto.getCategory().getName()));

        //VERIFICAÇÃO
        Mockito.verify(productService, Mockito.times(1))
                .findById(existingId);
    }

    //ID NÃO EXISTENTE
    @Test
    @DisplayName("Busca por ID deve lança Resource Not Found Exception quando ID não existe")
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(productService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/products/{id}", nonExistingId)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("PRODUCT_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO STATUS HTTP
        resultActions.andExpect(status().isNotFound());
    }

    //BUSCA POR SKU
    @Test
    @DisplayName("Busca por sku retorna Produto DTO")
    void findBySkuReturnProductDTO() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(productService.findBySku(existingSku)).thenReturn(productDto);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/products/sku")
                        .param("sku", existingSku)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("PRODUCT_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JSON
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.name").value(productDto.getName()));

        //VERIFICAÇÃO
        Mockito.verify(productService, Mockito.times(1))
                .findBySku(existingSku);
    }

    //BUSCA SKU INEXISTENTE
    @Test
    @DisplayName("Busca por SKU deve lança Resource Not Found Exception quando ID não existe")
    void findBySkuShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(productService.findBySku(nonExistingSku)).thenThrow(ResourceNotFoundException.class);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/products/sku")
                        .param("sku", nonExistingSku)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("PRODUCT_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO STATUS HTTP
        resultActions.andExpect(status().isNotFound());
    }

    //BUSCA TODOS PRODUTOS
    @Test
    @DisplayName("Busca todos deve retorna Lista de produtos DTO")
    void findAllShouldReturnListOfProductsDTO() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(productService.findAll(ArgumentMatchers.any())).thenReturn(page);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/products?page=0&size=10")
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("PRODUCT_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JSON
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(existingId))
                .andExpect(jsonPath("$.content[0].name").value(productDto.getName()));

        //VERIFICAÇÃO
        Mockito.verify(productService, Mockito.times(1))
                .findAll(ArgumentMatchers.any());
    }

    //BUSCA TODOS PRODUTOS POR CATEGORIA
    @Test
    @DisplayName("Busca todos produtos por categoria deve retorna Page de produtos DTO")
    void findProductsByCategoryNameShouldReturnPageOfProductsDTO() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(productService.findProductsByCategoryName(ArgumentMatchers.eq(category), ArgumentMatchers.any())).thenReturn(page);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/products/category")
                        .param("category", category)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("PRODUCT_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JSON
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(existingId))
                .andExpect(jsonPath("$.content[0].name").value(productDto.getName()))
                .andExpect(jsonPath("$.content[0].category.name").value(productDto.getCategory().getName()));

        //VERIFICAÇÃO
        Mockito.verify(productService, Mockito.times(1))
                .findProductsByCategoryName(ArgumentMatchers.eq(category), ArgumentMatchers.any());
    }

    //BUSCA PRODUTOS POR NOME
    @Test
    @DisplayName("Busca produtos por nome deve retorna Page de produtos DTO")
    void searchProductByNameShouldReturnPageOfProductsDTO() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(productService.searchProductByName(ArgumentMatchers.eq(productName), ArgumentMatchers.any())).thenReturn(page);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/products/search")
                        .param("name", productName)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("PRODUCT_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JSON
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(existingId))
                .andExpect(jsonPath("$.content[0].name").value(productDto.getName()));

        //VERIFICAÇÃO
        Mockito.verify(productService, Mockito.times(1))
                .searchProductByName(ArgumentMatchers.eq(productName), ArgumentMatchers.any());
    }

    //BUSCA POR NOME
    @Test
    @DisplayName("Busca por nome retorna Produto DTO")
    void findByNameReturnProductDTO() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(productService.findByName(productName)).thenReturn(productDto);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/products/name")
                        .param("name", productName)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("PRODUCT_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JSON
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.name").value(productDto.getName()));

        //VERIFICAÇÃO
        Mockito.verify(productService, Mockito.times(1))
                .findByName(productName);
    }

    // BUSCA TODOS PRODUTOS POR STATUS
    @Test
    @DisplayName("Busca todos produtos por status retorna Page de produtos DTO")
    void findAllProductsStatusShouldReturnPageOfProductsDTO() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(productService.findAllProductsStatus(ArgumentMatchers.eq(active), ArgumentMatchers.any())).thenReturn(page);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/products/status")
                        .param("active", String.valueOf(active))
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("PRODUCT_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JSON
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(existingId))
                .andExpect(jsonPath("$.content[0].name").value(productDto.getName()));

        //VERIFICAÇÃO
        Mockito.verify(productService, Mockito.times(1))
                .findAllProductsStatus(ArgumentMatchers.eq(active), ArgumentMatchers.any());
    }

    // ================= DELETE =================

    //ID EXISTENTE
    @Test
    @DisplayName("Delete deve deletar product quando ID existir")
    void deleteShouldDeleteProductWhenIdExists() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.doNothing().when(productService).deleteProduct(existingId);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(delete("/products/{id}", existingId)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("PRODUCT_WRITE")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO STATUS HTTP
        resultActions.andExpect(status().isNoContent());
    }

    // ID NÂO EXISTENTE
    @Test
    @DisplayName("Delete deve lançar Resource Not Found quando ID não existir")
    void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.doThrow(ResourceNotFoundException.class).when(productService).deleteProduct(nonExistingId);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(delete("/products/{id}", nonExistingId)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("PRODUCT_WRITE")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO STATUS HTTP
        resultActions.andExpect(status().isNotFound());
    }

    // ================= POST =================

    //DTO VALIDO
    @Test
    @DisplayName("Criar produto deve retornar produto DTO quando DTO Valido")
    void createProductShouldReturnProductDTOWhenDTOValid() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(productService.save(ArgumentMatchers.any())).thenReturn(productDto);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(post("/products")
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("PRODUCT_WRITE")
                        ))
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JSON
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.name").value(productDto.getName()));

        //VERIFICAÇÃO
        Mockito.verify(productService, Mockito.times(1))
                .save(ArgumentMatchers.any());
    }

    //DTO INVALIDO
    @Test
    @DisplayName("Criar Produto deve retornar Bad Request quando DTO invalido")
    void createProductShouldReturnBadRequestWhenDTOInvalid() throws Exception {

        // PREPARA - DTO INVÁLIDO
        productDto.setName(null);
        String jsonBodyInvalido = objectMapper.writeValueAsString(productDto);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(post("/products")
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("PRODUCT_WRITE")
                        ))
                        .content(jsonBodyInvalido)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - STATUS HTTP
        resultActions.andExpect(status().isBadRequest());

        //VERIFICAÇÃO
        Mockito.verify(productService, Mockito.never())
                .save(ArgumentMatchers.any());
    }

    // ================= PUT =================

    //ID VALIDO
    @Test
    @DisplayName("update deve atualizar retornar Produto DTO quando ID valido")
    void updateShouldReturnProductDTOWhenIDValid() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(productService.update(ArgumentMatchers.eq(existingId), ArgumentMatchers.any())).thenReturn(productDto);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(put("/products/{id}", existingId)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("PRODUCT_WRITE")
                        ))
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JSON
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.name").value(productDto.getName()));


        //VERIFICAÇÃO
        Mockito.verify(productService, Mockito.times(1))
                .update(ArgumentMatchers.eq(existingId), ArgumentMatchers.any());
    }

    //ID NAO EXISTENTE
    @Test
    @DisplayName("update deve lança Resource Not Found Exception quando ID não existe")
    void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(productService.update(ArgumentMatchers.eq(nonExistingId), ArgumentMatchers.any())).thenThrow(ResourceNotFoundException.class);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(put("/products/{id}", nonExistingId)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("PRODUCT_WRITE")
                        ))
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO STATUS HTTP
        resultActions.andExpect(status().isNotFound());
    }
}
