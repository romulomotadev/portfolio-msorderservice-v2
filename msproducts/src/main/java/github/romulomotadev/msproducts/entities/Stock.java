package github.romulomotadev.msproducts.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "tb_stock")
@NoArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantity;
    private Integer minQuantity;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
