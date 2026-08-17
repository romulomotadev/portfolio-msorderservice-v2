package github.romulomotadev.msclients.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import github.romulomotadev.msclients.entities.Client;
import github.romulomotadev.msclients.entities.Person;
import github.romulomotadev.msclients.entities.Type;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder( { "id", "type", "document"})
public class PersonDto {

    private Long id;
    private Type type;
    @NotBlank(message = "Document is required")
    private String document;

    public PersonDto(Person entity) {
        this.id = entity.getId();
        this.type = entity.getType();
        this.document = entity.getDocument();
    }
}
