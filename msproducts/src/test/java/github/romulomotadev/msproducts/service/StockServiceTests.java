package github.romulomotadev.msproducts.service;

import github.romulomotadev.msproducts.dto.CategoryDto;
import github.romulomotadev.msproducts.dto.ProductDto;
import github.romulomotadev.msproducts.dto.StockDto;
import github.romulomotadev.msproducts.entities.Product;
import github.romulomotadev.msproducts.entities.Stock;
import github.romulomotadev.msproducts.exception.exceptions.ResourceNotFoundException;
import github.romulomotadev.msproducts.repository.ProductRepository;
import github.romulomotadev.msproducts.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static github.romulomotadev.msproducts.factory.ProductFactory.createdProduct;
import static github.romulomotadev.msproducts.factory.StockFactory.createdStock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;


@ExtendWith(MockitoExtension.class)
public class StockServiceTests {


    @Mock
    private StockRepository stockRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private StockService stockService;

    private Long existingId;
    private Long nonExistingId;

    private Product product;
    private ProductDto productDto;

    private Stock stock;
    private StockDto stockDto;


    @BeforeEach
    void setUp() {

        existingId = 1L;
        nonExistingId = 2L;

        product = createdProduct();
        productDto = new ProductDto(product);

        stock = createdStock();
        stockDto = new StockDto(stock);
    }


    //============ UPDATE ===============//

    // ID PRODUTO EXISTENTE
    @Test
    @DisplayName(" update deve atualizar estoque quando produto Id existir")
    void updateShouldUpdateStockWhenProductIdExists() {

        //PREPARAR
        when(productRepository.existsById(existingId)).thenReturn(true);
        when(productRepository.getReferenceById(existingId)).thenReturn(product);
        when(stockRepository.getReferenceById(existingId)).thenReturn(stock);
        when(productRepository.save(any())).thenReturn(product);

        //EXECUTAR
        StockDto result = stockService.update(stockDto, existingId);

        //VERIFICAR
        assertNotNull(result);
        assertEquals(stockDto.getId(), result.getId());
        assertEquals(stockDto.getQuantity(), result.getQuantity());
        assertEquals(stockDto.getMinQuantity(), result.getMinQuantity());
    }

    //ID PRODUTO INEXISTENTE
    @Test
    @DisplayName(" update deve lançar Resource Not Found Exception quando produto Id não existir")
    void updateShouldThrowResourceNotFoundExceptionWhenProductIdNotExists(){

        //PREPARAR
        when(productRepository.existsById(nonExistingId)).thenReturn(false);

        //EXECUTAR + VERIFICAR
        assertThrows(ResourceNotFoundException.class,
                () -> stockService.update(stockDto, nonExistingId));
    }


    //============ GET ===============//

    // ID PRODUTO EXISTENTE
    @Test
    @DisplayName("findById deve retornar Stock DTO quando Produto Id existir")
    void findByIdShouldReturnStockDtoWhenIdProductExists() {

        //PREPARAR
        when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        when(stockRepository.getReferenceById(existingId)).thenReturn(stock);

        //EXECUTA
        StockDto result = stockService.findById(existingId);

        //VERIFICAR
        assertNotNull(result);
        assertEquals(stockDto.getId(), result.getId());
        assertEquals(stockDto.getQuantity(), result.getQuantity());
        assertEquals(stockDto.getMinQuantity(), result.getMinQuantity());
    }

    //ID PRODUTO INEXISTENTE
    @Test
    @DisplayName("findById deve lançar Resource Not Found Exception quando Produto Id não existir ")
    void findByIdShouldThrowResourceNotFoundExceptionWhenProductIdNotExists(){

        //PREPARA
        when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        //EXECUTA + VERIFICAR
        assertThrows(ResourceNotFoundException.class,
                () -> stockService.findById(nonExistingId));
    }

    //BUSCA ESTOQUE TODOS PRODUTOS EXISTENTES
    @Test
    @DisplayName("findAll deve retornar lista de Stock DTO quando existir estoque")
    void findAllShouldReturnStockDtoListWhenStockExists(){

        //PREPARA
        when(stockRepository.findAll()).thenReturn(List.of(stock));

        //EXECUTA
        List<StockDto> result = stockService.findAll();

        //VERIFICAR
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(stockDto.getId(), result.get(0).getId());
        assertEquals(stockDto.getQuantity(), result.get(0).getQuantity());
        assertEquals(stockDto.getMinQuantity(), result.get(0).getMinQuantity());
    }

    // BUSCA POR TODAS LISTA VAZIA
    @Test
    @DisplayName("findAll deve retornar lista vazia quando não houver dados")
    void findAllShouldReturnEmptyListWhenNoData() {

        // PREPARA
        when(stockRepository.findAll()).thenReturn(List.of());

        // EXECUTA
        List<StockDto> result = stockService.findAll();

        // VERIFICA
        assertTrue(result.isEmpty());

        verify(stockRepository).findAll();
        verifyNoMoreInteractions(stockRepository);
    }
}
