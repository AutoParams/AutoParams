package examples;

public final class Review {

    private final Product product;
    private final User reviewer;
    private final int rating;
    private final String comment;

    public Review(
        Product product,
        User reviewer,
        int rating,
        String comment
    ) {
        this.product = product;
        this.reviewer = reviewer;
        this.rating = rating;
        this.comment = comment;
    }

    public Product getProduct() {
        return product;
    }

    public User getReviewer() {
        return reviewer;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }
}
