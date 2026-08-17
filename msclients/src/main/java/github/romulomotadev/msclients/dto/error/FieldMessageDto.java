package github.romulomotadev.msclients.dto.error;

import lombok.Getter;

@Getter
public class FieldMessageDto {

    //GETTER
    //ATRIBUTOS
    private final String fieldName;
    private final String message;

    //CONSTRUTORES
    public FieldMessageDto(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

}
