package github.romulomotadev.msproducts.category;

import github.romulomotadev.msproducts.dto.CategoryDto;
import github.romulomotadev.msproducts.entities.Category;
import github.romulomotadev.msproducts.exception.exceptions.DataDuplicateException;
import github.romulomotadev.msproducts.exception.exceptions.ResourceNotFoundException;
import github.romulomotadev.msproducts.repository.CategoryRepository;
import github.romulomotadev.msproducts.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    private String nameExisting;

    private Category category;
    private CategoryDto categoryDto;


    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 999L;

        category = createdCategory();
        categoryDto = new CategoryDto(category);

        nameExisting = category.getName();

    }


    // ======== POST =========

    // CREATED NEW CATEGORY
    @Test
    @DisplayName("create deve salvar e retornar DTO")
    void create_ShouldSaveAndReturnDTO() {

        // ARRANGE
        when(repository.existsByName(nameExisting)).thenReturn(false);
        when(repository.save(any(Category.class))).thenReturn(category);

        // ACT
        CategoryDto result = service.save(categoryDto);

        // ASSERT
        assertNotNull(result);
        assertEquals(category.getId(), result.getId());
        assertEquals(category.getName(), result.getName());

        verify(repository).save(any(Category.class));
        verifyNoMoreInteractions(repository);
    }

    // NON CREATED CATEGORY FOR DUPLICATE CATEGORY
    @Test
    @DisplayName("create deve lançar exceção quando a categoria já existe")
    void create_ShouldThrowExceptionDuplicated() {
        // ARRANGE
        when(repository.existsByName(categoryDto.getName())).thenReturn(true);

        // ACT
        DataDuplicateException exception = assertThrows(
                DataDuplicateException.class,
                () -> service.save(categoryDto)
        );

        // ASSERT
        assertEquals("Category already exists", exception.getMessage());
        verify(repository).existsByName(categoryDto.getName());
        verify(repository, never()).save(any(Category.class));
        verifyNoMoreInteractions(repository);
    }


    // ======== GET =========

    // FIND BY ID
    @Test
    @DisplayName("findById deve retornar DTO quando ID existir")
    void findById_ShouldReturnDTO_WhenIdExists() {

        // ARRANGE
        when(repository.findById(existingId)).thenReturn(Optional.of(category));

        // ACT
        CategoryDto result = service.findById(existingId);

        // ASSERT
        assertNotNull(result);
        assertEquals(existingId, result.getId());
        assertEquals(category.getName(), result.getName());

        verify(repository).findById(existingId);
        verifyNoMoreInteractions(repository);
    }

    // FIND BY ID NOT EXISTS
    @Test
    @DisplayName("findById deve lançar exceção quando ID não existir")
    void findById_ShouldThrowException_WhenIdDoesNotExist() {

        // ARRANGE
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });

        verify(repository).findById(nonExistingId);
        verifyNoMoreInteractions(repository);
    }

    // SEARCH BY NAME
    @Test
    @DisplayName("searchByName deve retornar DTO quando nome existir")
    void searchByName_ShouldReturnDTO_WhenNameExists() {

        //ARRANGE
        when(repository.searchByName(category.getName())).thenReturn(List.of(category));

        // ACT
        List<CategoryDto> result = service.searchByName(category.getName());

        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(category.getName(), result.getFirst().getName());
    }

    // SEARCH BY NAME WHEN LIST IS EMPTY
    @Test
    @DisplayName("searchByName deve retornar lista vazia quando não houver dados")
    void searchByName_ShouldReturnEmptyList_WhenNoData(){

        // ARRANGE
        when(repository.searchByName(category.getName())).thenReturn(List.of());

        // ACT
        List<CategoryDto> result = service.searchByName(categoryDto.getName());

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository).searchByName(categoryDto.getName());
        verifyNoMoreInteractions(repository);
    }

    // FIND ALL
    @Test
    @DisplayName("findAll deve retornar lista de DTO")
    void findAll_ShouldReturnListOfDTO() {

        // ARRANGE
        when(repository.findAll()).thenReturn(List.of(category));

        // ACT
        List<CategoryDto> result = service.findAll();

        // ASSERT
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
    }

    // FIND ALL WHEN LIST IS EMPTY
    @Test
    @DisplayName("findAll deve retornar lista vazia quando não houver dados")
    void findAll_ShouldReturnEmptyList_WhenNoData() {

        // ARRANGE
        when(repository.findAll()).thenReturn(List.of());

        // ACT
        List<CategoryDto> result = service.findAll();

        // ASSERT
        assertTrue(result.isEmpty());

        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
    }


    // ======== PUT =========

    // UPDATE ID EXISTING
    @Test
    @DisplayName("update deve atualizar e retornar DTO quando ID existir")
    void update_ShouldUpdateAndReturnDTO_WhenIdExists() {

        // ARRANGE
        when(repository.existsByName(category.getName())).thenReturn(false);
        when(repository.findById(existingId)).thenReturn(Optional.of(category));
        when(repository.save(any(Category.class))).thenReturn(category);

        // ACT
        CategoryDto result = service.update(existingId, categoryDto);

        // ASSERT
        assertNotNull(result);
        assertEquals(existingId, result.getId());
        assertEquals(category.getName(), result.getName());

        verify(repository).findById(existingId);
        verify(repository).save(any(Category.class));
        verifyNoMoreInteractions(repository);
    }

    // UPDATE ID NOT EXIST
    @Test
    @DisplayName("update deve lançar exceção quando ID não existir")
    void update_ShouldThrowException_WhenIdDoesNotExist() {

        // ARRANGE
        when(repository.existsByName(category.getName()))
                .thenReturn(false);
        when(repository.findById(nonExistingId))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, categoryDto);
        });

        verify(repository).findById(nonExistingId);
        verifyNoMoreInteractions(repository);
    }

    // UPDATE CATEGORY EXISTING
    @Test
    @DisplayName("update deve lançar exceção quando category já existir")
    void update_ShouldThrowException_WhenCategoryExist() {

        // ARRANGE
        when(repository.existsByName(category.getName())).thenReturn(true);

        // ACT + ASSERT
        assertThrows(DataDuplicateException.class, () -> {
            service.update(existingId, categoryDto);
        });

        verify(repository).existsByName(category.getName());
        verifyNoMoreInteractions(repository);
    }


    // ======== DELETE =========

    // DELETE ID EXISTING
    @Test
    @DisplayName("delete deve remover quando ID existir")
    void delete_ShouldRemove_WhenIdExists() {

        // ARRANGE
        when(repository.existsById(existingId))
                .thenReturn(true);

        // ACT
        service.delete(existingId);

        // ASSERT
        verify(repository).existsById(existingId);
        verify(repository).deleteById(existingId);
        verifyNoMoreInteractions(repository);
    }

    // DELETE ID NOT EXISTING
    @Test
    @DisplayName("delete deve lançar exceção quando ID não existir")
    void delete_ShouldThrowException_WhenIdDoesNotExist() {

        // ARRANGE
        when(repository.existsById(nonExistingId))
                .thenReturn(false);

        // ACT + ASSERT
        assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });

        verify(repository).existsById(nonExistingId);
        verifyNoMoreInteractions(repository);
    }
}
