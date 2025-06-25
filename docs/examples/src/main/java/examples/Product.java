package examples;

import java.math.BigDecimal;

public class Product {

    private final String name;
    private final String imageUri;
    private final String description;
    private final BigDecimal priceAmount;
    private final int stockQuantity;

    public Product(String name, String imageUri, String description,
                   BigDecimal priceAmount, int stockQuantity) {
        this.name = name;
        this.imageUri = imageUri;
        this.description = description;
        this.priceAmount = priceAmount;
        this.stockQuantity = stockQuantity;
    }

    public String getName() {
        return name;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPriceAmount() {
        return priceAmount;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }
}
