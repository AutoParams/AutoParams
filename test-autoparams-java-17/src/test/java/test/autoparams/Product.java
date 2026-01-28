package test.autoparams;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record Product(
    UUID id,
    String name,
    Category category,
    String imageUri,
    String description,
    BigDecimal priceAmount,
    int stockQuantity,
    List<String> tags
) {
}
