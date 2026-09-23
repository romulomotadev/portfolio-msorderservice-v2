package github.romulomotadev.msorderservice.factory;

import github.romulomotadev.msorderservice.dto.ProductResponseDto;

public class ProductDataResponseDtoFactory {

    public static ProductResponseDto createProductResponseDto() {

        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setId(1L);
        productResponseDto.setName("Produto 1");
        productResponseDto.setDescription("descrição do produto 1");
        productResponseDto.setSku("sku-1");
        productResponseDto.setPrice(100.0);
        productResponseDto.setActive(true);

        return productResponseDto;
    }
}
