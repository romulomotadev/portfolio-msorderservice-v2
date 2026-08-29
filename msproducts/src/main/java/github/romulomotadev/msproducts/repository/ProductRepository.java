package github.romulomotadev.msproducts.repository;

import github.romulomotadev.msproducts.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // BUSCA PRODUTO PELO CODIGO SKU
    Product findBySku(String sku);

    // BUSCA PRODUTOS POR CATEGORIA
    Page<Product> findProductsByCategoryName(String categoryName, Pageable pageable);

    // ENCONTRAR PRODUTOS POR NOME
    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM TB_PRODUCT " +
                    "WHERE UPPER(TB_PRODUCT.NAME) LIKE UPPER('%' || :name || '%')",
            countQuery = "SELECT COUNT(*) " +
                    "FROM TB_PRODUCT " +
                    "WHERE UPPER(TB_PRODUCT.NAME) LIKE UPPER('%' || :name || '%')")
    Page<Product> searchProductByName(@Param("name") String name, Pageable pageable);

    // BUSCA TODOS PRODUTOS ATIVOS OU INATIVOS
    @Query(nativeQuery = true,
            value = "SELECT * " +
            "FROM TB_PRODUCT " +
            "WHERE TB_PRODUCT.ACTIVE = :status",
            countQuery = "SELECT COUNT(*) " +
                    "FROM TB_PRODUCT " +
                    "WHERE TB_PRODUCT.ACTIVE = :status")
    Page<Product> findAllProductsStatus(Boolean status, Pageable pageable);

}
