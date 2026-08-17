package github.romulomotadev.msclients.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import github.romulomotadev.msclients.entities.Address;
import github.romulomotadev.msclients.entities.Client;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@JsonPropertyOrder( { "id", "name", "email", "person", "addresses"})
public class ClientDto {

    private Long id;
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Email is required")
    private String email;
    @Valid
    private PersonDto person;
    @Valid
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
