package github.romulomotadev.msproducts.repository;

import github.romulomotadev.msproducts.entities.Category;
import github.romulomotadev.msproducts.factory.CategoryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

@DataJpaTest
public class CategoryRepositoryTests {

    //================== DEPENDÊNCIAS ==================

    @Autowired
    private CategoryRepository categoryRepository;

    //================== ATRIBUTOS ==================

    private Category category;

    //================== INICIALIZAÇÃO ==================

    @BeforeEach
    void setUp() {

        //Factory
        category = CategoryFactory.createdCategory();
        category.setId(null);

        categoryRepository.save(category);
    }

    //================== GET ==================

    //NOME EXISTENTE
    @Test
    @DisplayName("Busca por nome deve retornar categoria se nome existir")
    void searchByNameShouldReturnCategoriesWhenNameExists(){

        //PREPARA
        String existingName = category.getName();

        //EXECUTA
        List<Category> result = categoryRepository.searchByName(existingName);

        //VALIDAÇÃO
        Assertions.assertNotNull(result);
        Assertions.assertEquals(category.getId(), result.getFirst().getId());
        Assertions.assertEquals(category.getName(), result.getFirst().getName());
    }

    //NOME INEXISTENTE
    @Test
    @DisplayName("Deve retornar lista vazia quando categoria não existir")
    void searchByNameShouldReturnEmptyListWhenCategoryDoesNotExist() {

        //PREPARA
        String nonExistingName = "nonName";

        //EXECUTA
        List<Category> result = categoryRepository.searchByName(nonExistingName);

        //VALIDAÇÃO
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    //IGNORE CASE
    //NOME EXISTENTE
    @Test
    @DisplayName("Busca por nome deve ignorar maiúsculas e minúsculas")
    void searchByNameShouldIgnoreCase(){

        //PREPARA
        String ignoreCaseName = "CaTeGory 1";

        //EXECUTA
        List<Category> result = categoryRepository.searchByName(ignoreCaseName);

        //VALIDAÇÃO
        Assertions.assertNotNull(result);
        Assertions.assertEquals(category.getId(), result.getFirst().getId());
        Assertions.assertEquals(category.getName(), result.getFirst().getName());
    }
}
