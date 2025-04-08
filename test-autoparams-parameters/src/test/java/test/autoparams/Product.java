package test.autoparams;

import java.math.BigDecimal;
import java.util.UUID;

public record Product(
    UUID id,
    String name,
    String imageUri,
    String description,
    BigDecimal priceAmount,
    int stockQuantity
) {
}
