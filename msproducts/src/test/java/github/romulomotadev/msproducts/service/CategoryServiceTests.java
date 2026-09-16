package github.romulomotadev.msproducts.service;

import github.romulomotadev.msproducts.dto.CategoryDto;
import github.romulomotadev.msproducts.entities.Category;
import github.romulomotadev.msproducts.exception.exceptions.DataDuplicateException;
import github.romulomotadev.msproducts.exception.exceptions.ResourceNotFoundException;
import github.romulomotadev.msproducts.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static github.romulomotadev.msproducts.factory.CategoryFactory.createdCategory;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTests {

    @InjectMocks
    private CategoryService service;
    @Mock
    private CategoryRepository repository;


    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;

    private String nameExisting;

    private Category category;
    private CategoryDto categoryDto;


    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;

        category = createdCategory();
        categoryDto = new CategoryDto(category);

        nameExisting = category.getName();
    }


    // ======== SAVE =========

    // SALVA NOVA CATEGORIA
    @Test
    @DisplayName("save deve salvar e retornar Category DTO")
    void saveShouldSaveAndReturnCategoryDTO() {

        // PREPARA
        when(repository.existsByName(nameExisting)).thenReturn(false);
        when(repository.save(any(Category.class))).thenReturn(category);

        // EXECUTA
        CategoryDto result = service.save(categoryDto);

        // VERIFICA
        assertNotNull(result);
        assertEquals(category.getId(), result.getId());
        assertEquals(category.getName(), result.getName());

        verify(repository).save(any(Category.class));
        verifyNoMoreInteractions(repository);
    }


    // NAO IRA SAVA QUANDO CATEGORIA JA EXISTENTE
    @Test
    @DisplayName("save deve lançar Data Duplicate Exception quando a categoria já existe")
    void saveShouldThrowDataDuplicateExceptionExceptionWhenCategoryAlreadyExists() {

        // PREPARA
        when(repository.existsByName(categoryDto.getName())).thenReturn(true);

        // EXECUTA + VERIFICA
        assertThrows(DataDuplicateException.class,
                () -> service.save(categoryDto)
        );
    }


    // ======== GET =========

    // BUSCA POR ID
    @Test
    @DisplayName("findById deve retornar Category DTO quando ID existir")
    void findByIdShouldReturnCategoryDTOWhenIdExists() {

        // PREPARA
        when(repository.findById(existingId)).thenReturn(Optional.of(category));

        // EXECUTA
        CategoryDto result = service.findById(existingId);

        // VERIFICA
        assertNotNull(result);
        assertEquals(existingId, result.getId());
        assertEquals(category.getName(), result.getName());

        verify(repository).findById(existingId);
        verifyNoMoreInteractions(repository);
    }

    // BUSCA POR ID NAO EXISTENTE
    @Test
    @DisplayName("findById deve lançar Resource Not Found Exception quando ID não existir")
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        // PREPARA
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        // EXECUTA + VERIFICA
        assertThrows(ResourceNotFoundException.class, () ->
            service.findById(nonExistingId));
    }

    // BUSCA POR NOME
    @Test
    @DisplayName("searchByName deve retornar Category DTO quando Categoria já existir")
    void searchByNameShouldReturnDTOWhenCategoryAlreadyExists() {

        //PREPARA
        when(repository.searchByName(category.getName())).thenReturn(List.of(category));

        // EXECUTA
        List<CategoryDto> result = service.searchByName(category.getName());

        // VERIFICA
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(category.getName(), result.getFirst().getName());
    }

    // BUSCA POR NOME QUANDO NOME NAO EXISTE
    @Test
    @DisplayName("searchByName deve retornar lista vazia quando não houver categoria")
    void searchByNameShouldReturnEmptyListWhenCategory(){

        // PREPARA
        when(repository.searchByName(category.getName())).thenReturn(List.of());

        // EXECUTA
        List<CategoryDto> result = service.searchByName(categoryDto.getName());

        // VERIFICA
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository).searchByName(categoryDto.getName());
        verifyNoMoreInteractions(repository);
    }

    // BUSCA POR TODAS AS CATEGORIAS
    @Test
    @DisplayName("findAll deve retornar lista de Categorias DTOs")
    void findAllShouldReturnListOfCategoriesDTOs() {

        // PREPARA
        when(repository.findAll()).thenReturn(List.of(category));

        // EXECUTA
        List<CategoryDto> result = service.findAll();

        // VERIFICA
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
    }

    // BUSCA POR TODAS AS CATEGORIAS QUANDO NAO EXISTE
    @Test
    @DisplayName("findAll deve retornar lista vazia quando não houver dados")
    void findAllShouldReturnEmptyListWhenNoData() {

        // PREPARA
        when(repository.findAll()).thenReturn(List.of());

        // EXECUTA
        List<CategoryDto> result = service.findAll();

        // VERIFICA
        assertTrue(result.isEmpty());

        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
    }


    // ======== UPDATE =========

    // ATUALIZA ID NÃO EXISTE
    @Test
    @DisplayName("update deve atualizar e retornar Category DTO quando ID existir")
    void updateShouldUpdateAndReturnCategoryDTOWhenIdExists() {

        // PREPARA
        when(repository.existsByName(category.getName())).thenReturn(false);
        when(repository.findById(existingId)).thenReturn(Optional.of(category));
        when(repository.save(any(Category.class))).thenReturn(category);

        // EXECUTA
        CategoryDto result = service.update(existingId, categoryDto);

        // VERIFICA
        assertNotNull(result);
        assertEquals(existingId, result.getId());
        assertEquals(category.getName(), result.getName());

        verify(repository).findById(existingId);
        verify(repository).save(any(Category.class));
        verifyNoMoreInteractions(repository);
    }

    // ATUALIZA ID NAO EXISTE
    @Test
    @DisplayName("update deve lançar Resource Not Found Exception quando ID não existir")
    void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        // PREPARA
        when(repository.existsByName(category.getName())).thenReturn(false);
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        // EXECUTA + VERIFICA
        assertThrows(ResourceNotFoundException.class, () ->
            service.update(nonExistingId, categoryDto));
    }

    // NAO ATUALIZA QUANDO CATEGORIA JA EXISTE
    @Test
    @DisplayName("update deve lançar exceção quando category já existir")
    void update_ShouldThrowException_WhenCategoryExist() {

        // PREPARA
        when(repository.existsByName(category.getName())).thenReturn(true);

        // EXECUTA + VERIFICA
        assertThrows(DataDuplicateException.class, () ->
            service.update(existingId, categoryDto));
    }


    // ======== DELETE =========

    // ID EXISTE
    @Test
    @DisplayName("delete deve remover categoria quando ID existir")
    void deleteShouldRemoveCategoryWhenIdExists() {

        // PREPARA
        when(repository.existsById(existingId)).thenReturn(true);
        doNothing().when(repository).deleteById(existingId);

        // EXECUTA
        service.delete(existingId);

        // VERIFICA
        verify(repository).existsById(existingId);
        verify(repository).deleteById(existingId);
        verifyNoMoreInteractions(repository);
    }

    // ID NAO EXISTE
    @Test
    @DisplayName("delete deve lançar exceção Resource Not Found Exception quando ID não existir")
    void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        // PREPARA
        when(repository.existsById(nonExistingId)).thenReturn(false);

        // EXECUTA + VERIFICA
        assertThrows(ResourceNotFoundException.class, () ->
            service.delete(nonExistingId));
    }
}
