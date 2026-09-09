package github.romulomotadev.msproducts.factory;

import github.romulomotadev.msproducts.entities.Product;

import java.time.Instant;

import static github.romulomotadev.msproducts.factory.CategoryFactory.createdCategory;

public class ProductFactory {

    public static Product createdProduct(){

        Product product = new Product();
        product.setId(1L);
        product.setName("Product 1");
        product.setDescription("Product description");
        product.setSku("SKU1");
        product.setActive(true);
        product.setCreatedAt(Instant.now());
        product.setPrice(100.0);

        product.setCategory(createdCategory());

        return product;
    }

}
