package examples;

public class Product {

    private final String name;
    private final String imageUri;
    private final String description;
    private final double price;
    private final int stock;

    public Product(
        String name,
        String imageUri,
        String description,
        double price,
        int stock
    ) {
        this.name = name;
        this.imageUri = imageUri;
        this.description = description;
        this.price = price;
        this.stock = stock;
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

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }
}
