package test.autoparams;

import java.util.UUID;

public record Review(
    UUID id,
    UUID reviewerId,
    Product product,
    int rating,
    String comment
) {
}
