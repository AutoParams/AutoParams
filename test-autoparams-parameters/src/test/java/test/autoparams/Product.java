package test.autoparams;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Product {

    private final UUID id;
    private final String name;
    private final String imageUri;
    private final String description;
    private final BigDecimal priceAmount;
    private final int stockQuantity;
    private final boolean displayed;
}
