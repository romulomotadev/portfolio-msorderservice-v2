package github.romulomotadev.msproducts.factory;
import github.romulomotadev.msproducts.entities.Stock;


public class StockFactory {

    public static Stock createdStock() {

        Stock stock = new Stock();
        stock.setId(1L);
        stock.setQuantity(10);
        stock.setMinQuantity(5);

        return stock;
    }
}
