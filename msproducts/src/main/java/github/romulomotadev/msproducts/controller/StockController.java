package github.romulomotadev.msproducts.controller;

import github.romulomotadev.msproducts.dto.StockDto;
import github.romulomotadev.msproducts.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockController {

    public final StockService stockService;

    @PutMapping(value = "/{id}")
    public ResponseEntity <StockDto> updateStock(@PathVariable Long id, @RequestBody StockDto stockDto) {
        StockDto dto = stockService.update(stockDto, id);
        return ResponseEntity.ok(dto);
    }
}
