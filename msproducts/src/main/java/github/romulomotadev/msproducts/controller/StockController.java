package github.romulomotadev.msproducts.controller;

import github.romulomotadev.msproducts.dto.StockDto;
import github.romulomotadev.msproducts.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockController {

    public final StockService stockService;

    //============ UPDATE ===============//

    //UPDATE STOCK
    @PutMapping(value = "/{id}")
    public ResponseEntity <StockDto> updateStock(@PathVariable Long id, @RequestBody StockDto stockDto) {
        StockDto dto = stockService.update(stockDto, id);
        return ResponseEntity.ok(dto);
    }


    //============ GET ===============//

    // STOCK DO PRODUTO
    @GetMapping(value = "/{id}")
    public ResponseEntity<StockDto> findById(@PathVariable Long id) {
        StockDto dto = stockService.findById(id);
        return ResponseEntity.ok(dto);
    }

    // TODOS OS STOCKS
    @GetMapping
    public ResponseEntity<List<StockDto>> findAll() {
        List<StockDto> dto = stockService.findAll();
        return ResponseEntity.ok(dto);
    }
}
