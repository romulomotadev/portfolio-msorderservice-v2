package github.romulomotadev.msorderservice.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@JsonPropertyOrder({"id", "name", "email", "person", "addresses"})
public class ClientResponseDto {

    private Long id;
    private String name;
    private String email;
    private PersonResponseDto person;
    private List<AddressResponseDto> addresses = new ArrayList<>();

}
