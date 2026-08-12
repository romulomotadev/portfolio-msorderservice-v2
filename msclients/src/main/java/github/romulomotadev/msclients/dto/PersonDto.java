package github.romulomotadev.msclients.dto;

import github.romulomotadev.msclients.entities.Person;
import github.romulomotadev.msclients.entities.Type;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonDto {

    private Long id;
    private Type type;
    private String document;

    public PersonDto(Person entity) {
        this.id = entity.getId();
        this.type = entity.getType();
        this.document = entity.getDocument();
    }
}
