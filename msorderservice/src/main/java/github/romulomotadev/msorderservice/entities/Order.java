package github.romulomotadev.msorderservice.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String clientName;
    private String clientDocument;
    private LocalDateTime creationDate;
    private Double totalValue;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();


    // ========= METHODS ===========

    public void setTotalValue() {
        this.totalValue = 0.0;
        for (OrderItem orderItem : orderItems) {
            this.totalValue += orderItem.getTotalValue();
        }
    }
}
