package github.romulomotadev.msclients.dto;

import github.romulomotadev.msclients.entities.Address;
import github.romulomotadev.msclients.entities.Client;
import github.romulomotadev.msclients.entities.Person;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class ClientDto {

    private Long id;
    private String name;
    private String email;
    private String password;
    private Person person;
    private List<Address> addresses;

    public ClientDto(Client entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.password = entity.getPassword();
        this.person = entity.getPerson();
        this.addresses.addAll(entity.getAddresses());
    }
}
