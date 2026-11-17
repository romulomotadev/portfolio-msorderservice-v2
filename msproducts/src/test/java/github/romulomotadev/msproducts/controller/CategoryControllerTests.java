package github.romulomotadev.msproducts.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import github.romulomotadev.msproducts.config.KeycloakJwtConverter;
import github.romulomotadev.msproducts.config.SecurityConfig;
import github.romulomotadev.msproducts.dto.CategoryDto;
import github.romulomotadev.msproducts.entities.Category;
import github.romulomotadev.msproducts.exception.exceptions.ResourceNotFoundException;
import github.romulomotadev.msproducts.factory.CategoryFactory;
import github.romulomotadev.msproducts.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
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

@WebMvcTest(CategoryController.class)
@Import(SecurityConfig.class)
public class CategoryControllerTests {

    //================== DEPENDÊNCIAS ==================

    @MockitoBean
    private CategoryService categoryService;
    @MockitoBean
    private KeycloakJwtConverter keycloakJwtConverter;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    //================== ATRIBUTOS ==================

    private CategoryDto categoryDto;
    private Long existingId;
    private Long nonExistingId;
    private String categoryName;
    private String jsonBody;

    //================== INICIALIZAÇÃO ==================

    @BeforeEach
    void setUp() throws JsonProcessingException {
        existingId = 1L;
        nonExistingId = 2L;
        categoryName = "Category 1";

        //Factory
        Category category = CategoryFactory.createdCategory();
        categoryDto = new CategoryDto(category);

        //Corpo da requisição
        jsonBody = objectMapper.writeValueAsString(categoryDto);
    }

    //================== GET ==================

    //ID EXISTENTE
    @Test
    @DisplayName("Busca por ID deve retornar category quando ID existe")
    void findByIdShouldReturnCategoryDTOWhenIdExists() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(categoryService.findById(existingId)).thenReturn(categoryDto);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/category/{id}", existingId)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("PRODUCT_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JSON
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.name").value(categoryDto.getName()));

        //VERIFICAÇÃO
        Mockito.verify(categoryService, Mockito.times(1))
                .findById(existingId);
    }

    //GET ID NÃO EXISTENTE
    @Test
    @DisplayName("Busca por ID deve lança Resource Not Found Exception quando ID não existe")
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(categoryService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/category/{id}", nonExistingId)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("PRODUCT_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO STATUS HTTP
        resultActions.andExpect(status().isNotFound());
    }


    //BUSCA POR NOME
    @Test
    @DisplayName("Busca categorias por nome e retorna de Lista de categoria DTO")
    void findCategoryForNameShouldReturnListOfCategoryDTO() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(categoryService.searchByName(categoryName)).thenReturn(List.of(categoryDto));

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/category/search")
                        .param("name", categoryName)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("PRODUCT_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JSON
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(existingId))
                .andExpect(jsonPath("$[0].name").value(categoryDto.getName()));

        //VERIFICAÇÃO
        Mockito.verify(categoryService, Mockito.times(1))
                .searchByName(categoryName);
    }

    //BUSCA TODOS CATEGORIAS
    @Test
    @DisplayName("Busca todos deve retorna Lista de categorias DTO")
    void findAllShouldReturnListOfCategoryDTO() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(categoryService.findAll()).thenReturn(List.of(categoryDto));

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/category")
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("PRODUCT_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JSON
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(existingId))
                .andExpect(jsonPath("$[0].name").value(categoryDto.getName()));

        //VERIFICAÇÃO
        Mockito.verify(categoryService, Mockito.times(1))
                .findAll();
    }

    // ================= DELETE =================

    //ID EXISTENTE
    @Test
    @DisplayName("Delete deve deletar categoria quando ID existir")
    void deleteShouldDeleteClientWhenIdExists() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.doNothing().when(categoryService).delete(existingId);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(delete("/category/{id}", existingId)
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
        Mockito.doThrow(ResourceNotFoundException.class).when(categoryService).delete(nonExistingId);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(delete("/category/{id}", nonExistingId)
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
    @DisplayName("Save deve retornar categoria DTO quando DTO Valido")
    void saveShouldReturnCategoryDTOWhenDTOValid() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(categoryService.save(ArgumentMatchers.any())).thenReturn(categoryDto);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(post("/category")
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("PRODUCT_WRITE")
                        ))
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JSON
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.name").value(categoryDto.getName()));

        //VERIFICAÇÃO
        Mockito.verify(categoryService, Mockito.times(1))
                .save(ArgumentMatchers.any());
    }

    //DTO INVALIDO
    @Test
    @DisplayName("Save deve retornar Bad Request quando DTO invalido")
    void saveShouldReturnBadRequestWhenDTOInvalid() throws Exception {

        // PREPARA - DTO INVÁLIDO
        categoryDto.setName(null);
        String jsonBodyInvalido = objectMapper.writeValueAsString(categoryDto);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(post("/category")
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("PRODUCT_WRITE")
                        ))
                        .content(jsonBodyInvalido)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - STATUS HTTP
        resultActions.andExpect(status().isBadRequest());

        //VERIFICAÇÃO
        Mockito.verify(categoryService, Mockito.never())
                .save(ArgumentMatchers.any());
    }

    // ================= PUT =================

    //ID VALIDO
    @Test
    @DisplayName("update deve atualizar retornar Categoria DTO quando ID valido")
    void updateShouldReturnCategoryDTOWhenIDValid() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(categoryService.update(ArgumentMatchers.eq(existingId), ArgumentMatchers.any())).thenReturn(categoryDto);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(put("/category/{id}", existingId)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("PRODUCT_WRITE")
                        ))
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JSON
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.name").value(categoryDto.getName()));


        //VERIFICAÇÃO
        Mockito.verify(categoryService, Mockito.times(1))
                .update(ArgumentMatchers.eq(existingId), ArgumentMatchers.any());
    }

    //ID NAO EXISTENTE
    @Test
    @DisplayName("update deve lança Resource Not Found Exception quando ID não existe")
    void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(categoryService.update(ArgumentMatchers.eq(nonExistingId), ArgumentMatchers.any())).thenThrow(ResourceNotFoundException.class);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(put("/category/{id}", nonExistingId)
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
