package github.romulomotadev.msorderservice.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tb_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Integer quantity;
    private Double uniPrice;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;


    // ========= METHODS ===========

    public Double getTotalValue() {
        return quantity * uniPrice;
    }
}
