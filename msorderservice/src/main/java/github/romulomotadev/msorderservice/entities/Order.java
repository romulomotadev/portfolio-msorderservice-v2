package github.romulomotadev.msorderservice.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tb_orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long clientId;
    private LocalDateTime creationDate;
    private Double totalValeu;

    private RequestStatus requestStatus;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> items;


    // ========= METHODS ===========

    public Double getTotalValue() {

        for(OrderItem item : items){
            totalValeu += item.getTotalValue();
        }

        return totalValeu;
    }
}
