package org.javaunit.autoparams;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

final class UrlGenerator implements ObjectGenerator {

    private static final String[] PROTOCOLS = new String[] {
        "http",
        "https",
        "ftp"
    };

    @Override
    public GenerationResult generateObject(ObjectQuery query, ObjectGenerationContext context) {
        return query.getType().equals(URL.class)
            ? GenerationResult.presence(generate())
            : GenerationResult.absence();
    }

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        String message = "This method is not supported. Use generateObject method instead.";
        throw new UnsupportedOperationException(message);
    }

    private URL generate() {
        int index = ThreadLocalRandom.current().nextInt(PROTOCOLS.length);
        String urlSource = String.format("%s://auto.params", PROTOCOLS[index]);

        try {
            return new URL(urlSource);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}
