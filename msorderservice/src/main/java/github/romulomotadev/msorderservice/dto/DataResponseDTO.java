package github.romulomotadev.msorderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataResponseDTO {

    private ClientResponseDto clientResponseDto;
}
