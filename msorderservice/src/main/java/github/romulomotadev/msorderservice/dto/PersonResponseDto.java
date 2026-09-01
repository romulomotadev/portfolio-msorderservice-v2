package github.romulomotadev.msorderservice.dto;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder({"id", "type", "document"})
public class PersonResponseDto {

    private Long id;
    private String type;
    private String document;

}
