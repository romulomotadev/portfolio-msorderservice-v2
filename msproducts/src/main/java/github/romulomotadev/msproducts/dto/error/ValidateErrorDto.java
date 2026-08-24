package github.romulomotadev.msproducts.dto.error;

import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidateErrorDto extends CustomErrorDto {

    //GETTER
    private final List<FieldMessageDto> errors = new ArrayList<>();

    //CONSTRUTOR
    public ValidateErrorDto(Instant timestamp, Integer status, String error, String path) {
        super(timestamp, status, error, path);
    }

    //MÉTODOS
    public void addError(String fieldName, String message) {
        //Remover erros de todos os objetos cujo campo fieldName é igual à variável fieldName passada como parâmetro
        errors.removeIf(x -> x.getFieldName().equals(fieldName));
        errors.add(new FieldMessageDto(fieldName, message));
    }

}
