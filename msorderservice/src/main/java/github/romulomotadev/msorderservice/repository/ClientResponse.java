package github.romulomotadev.msorderservice.repository;

import github.romulomotadev.msorderservice.dto.ClientResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "msclients", path = "/clients")

public interface ClientResponse {

    @GetMapping(value = "/document")
    ResponseEntity<ClientResponseDto> findByPersonDocument(
            @RequestParam String document);
}
