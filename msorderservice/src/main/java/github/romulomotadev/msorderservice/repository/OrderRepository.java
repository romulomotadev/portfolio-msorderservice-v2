package github.romulomotadev.msorderservice.repository;

import github.romulomotadev.msorderservice.entities.Order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    //FIND BY ORDERS FOR STATUS
    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM TB_ORDERS " +
                    "WHERE UPPER(TB_ORDERS.REQUEST_STATUS) LIKE UPPER('%' || :status || '%')",
            countQuery = "SELECT COUNT(*) " +
                    "FROM TB_ORDERS " +
                    "WHERE UPPER(TB_ORDERS.REQUEST_STATUS) LIKE UPPER('%' || :status || '%')")
    Page<Order> findByStatus(String status, Pageable pageable);

    // FIND BY ORDERS FOR CLIENT
    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM TB_ORDERS " +
                    "WHERE UPPER(TB_ORDERS.CLIENT_NAME) LIKE UPPER('Bial Constantine')",
            countQuery = "SELECT COUNT(*) " +
                    "FROM TB_ORDERS " +
                    "WHERE UPPER(TB_ORDERS.CLIENT_NAME) LIKE UPPER('Bial Constantine')")
    Page<Order> findByClient(String nameClient, Pageable pageable);
}
