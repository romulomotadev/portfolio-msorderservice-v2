package github.romulomotadev.msproducts.repository;

import github.romulomotadev.msproducts.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // BUSCA CATEGORIA POR NOME
    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM TB_CATEGORY " +
                    "WHERE UPPER(TB_CATEGORY.NAME) LIKE UPPER('%' || :name || '%')")
    List<Category> searchByName(@Param("name") String name);

    // VERIFICAR CATEGORIA EM BANCO
    boolean existsByName(String name);
}
