package github.romulomotadev.msproducts.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class ProductMinDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validateProductMinDto_WhenValid_ShouldHaveNoViolations() {
        ProductMinDto dto = new ProductMinDto();
        dto.setName("Test Product");
        dto.setDescription("Test Description");
        dto.setSku("SKU123");
        dto.setPrice(10.0);
        dto.setActive(true);
        dto.setCategoryId(1L);

        Set<ConstraintViolation<ProductMinDto>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void validateProductMinDto_WhenNullValues_ShouldReturnViolations() {
        ProductMinDto dto = new ProductMinDto();

        Set<ConstraintViolation<ProductMinDto>> violations = validator.validate(dto);
        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting(ConstraintViolation::getPropertyPath)
                .extracting(Object::toString)
                .contains("name", "description", "sku", "price", "active", "categoryId");
    }

    @Test
    void validateProductMinDto_WhenPriceNegativeOrZero_ShouldReturnPositiveViolation() {
        ProductMinDto dto = new ProductMinDto();
        dto.setName("Test Product");
        dto.setDescription("Test Description");
        dto.setSku("SKU123");
        dto.setPrice(-5.0);
        dto.setActive(true);
        dto.setCategoryId(1L);

        Set<ConstraintViolation<ProductMinDto>> violations = validator.validate(dto);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("price")
                && v.getMessage().equals("Price must be positive"));
    }
}
