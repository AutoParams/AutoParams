package test.autoparams;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class Order {

    private String id;
    private BigDecimal originalPrice;
    private BigDecimal discountedPrice;

    @ConstructorProperties({"id", "originalPrice"})
    public Order(String id, BigDecimal originalPrice) {
        this.id = id;
        this.originalPrice = originalPrice;
        this.discountedPrice = originalPrice;
    }

    public void applyPercentDiscount(BigDecimal percentage) {
        BigDecimal rate = percentage.divide(BigDecimal.valueOf(100));
        BigDecimal amount = originalPrice.multiply(rate);
        this.discountedPrice = originalPrice.subtract(amount);
    }

    public void applyAmountDiscount(BigDecimal amount) {
        this.discountedPrice = this.discountedPrice.subtract(amount);
    }
}
