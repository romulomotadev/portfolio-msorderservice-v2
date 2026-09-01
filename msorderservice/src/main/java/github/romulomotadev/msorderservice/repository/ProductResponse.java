package github.romulomotadev.msorderservice.repository;

import github.romulomotadev.msorderservice.dto.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "msproducts", path = "/products")

public interface ProductResponse {

    @GetMapping("/search")
    ResponseEntity<Page<ProductResponseDto>> searchProductByName(
            @RequestParam String name, Pageable pageable);
}
