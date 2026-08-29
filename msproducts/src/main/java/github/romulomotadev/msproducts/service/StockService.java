package github.romulomotadev.msproducts.service;

import github.romulomotadev.msproducts.dto.ProductDto;
import github.romulomotadev.msproducts.dto.StockDto;
import github.romulomotadev.msproducts.entities.Product;
import github.romulomotadev.msproducts.entities.Stock;
import github.romulomotadev.msproducts.exception.exceptions.exceptions.ResourceNotFoundException;
import github.romulomotadev.msproducts.repository.ProductRepository;
import github.romulomotadev.msproducts.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;


    //============ UPDATE ===============//

    //UPDATE STOCK
    @Transactional
    public StockDto update(StockDto stockDto, Long id) {

        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("not found product with id: " + id));

        Stock entityStock = stockRepository.getReferenceById(product.getStock().getId());

        entityStock.setQuantity(stockDto.getQuantity());
        entityStock.setMinQuantity(stockDto.getMinQuantity());
        product.setStock(entityStock);

        productRepository.save(product);

        return new StockDto(entityStock);
    }


    //============ GET ===============//

    // STOCK DO PRODUTO
    @Transactional(readOnly = true)

    public StockDto findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("not found product with id: " + id));

        Stock stock = stockRepository.getReferenceById(product.getStock().getId());
        return new StockDto(stock);
    }

    // TODOS OS STOCKS
    @Transactional(readOnly = true)
    public List<StockDto> findAll() {

        List<Stock> stocks = stockRepository.findAll();

        return stocks.stream().map(StockDto::new).toList();
    }

}
