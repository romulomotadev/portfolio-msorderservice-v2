package github.romulomotadev.msorderservice.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@JsonPropertyOrder({"id", "address", "zipCode", "complement"})
public class AddressResponseDto {

    private Long id;
    private String address;
    private String zipCode;
    private String complement;

}
