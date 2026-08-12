package github.romulomotadev.msclients.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tb_clients")
@Data
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    private Person person;

    @OneToMany(mappedBy = "client")
    private List<Address> addresses;

}
