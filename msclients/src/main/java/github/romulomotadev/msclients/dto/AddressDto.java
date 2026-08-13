package github.romulomotadev.msclients.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import github.romulomotadev.msclients.entities.Address;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Data
@NoArgsConstructor
@JsonPropertyOrder({ "id", "address", "zipCode", "complement"})
public class AddressDto {

    private Long id;
    private String address;
    private String zipCode;
    private String complement;

    public AddressDto(Address entity) {
        this.id = entity.getId();
        this.address = entity.getAddress();
        this.zipCode = entity.getZipCode();
        this.complement = entity.getComplement();
    }
}
