package github.romulomotadev.msorderservice.repository;

import github.romulomotadev.msorderservice.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemsRespository extends JpaRepository<OrderItem, Long> {
}
