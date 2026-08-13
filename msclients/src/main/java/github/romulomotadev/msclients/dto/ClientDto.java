package github.romulomotadev.msclients.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import github.romulomotadev.msclients.entities.Address;
import github.romulomotadev.msclients.entities.Client;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@JsonPropertyOrder( { "id", "name", "email", "person", "addresses"})
public class ClientDto {

    private Long id;
    private String name;
    private String email;
    private PersonDto person;
    private List<AddressDto> addresses = new ArrayList<>();


    public ClientDto(Client entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.person = new PersonDto(entity.getPerson());

        for (Address address : entity.getAddresses()) {
            this.addresses.add(new AddressDto(address));
        }
    }
}
