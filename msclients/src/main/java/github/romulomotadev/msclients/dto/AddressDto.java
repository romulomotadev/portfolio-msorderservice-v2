package github.romulomotadev.msclients.dto;

import github.romulomotadev.msclients.entities.Address;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressDto {

    private Long id;
    private String address;
    private String zipCode;

    public AddressDto(Address entity) {
        this.id = entity.getId();
        this.address = entity.getAddress();
        this.zipCode = entity.getZipCode();
    }
}
