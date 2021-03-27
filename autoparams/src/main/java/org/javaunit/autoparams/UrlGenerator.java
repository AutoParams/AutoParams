package org.javaunit.autoparams;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;

final class UrlGenerator implements ObjectGenerator {

    private static final String[] PROTOCOLS = new String[] {
        "http",
        "https",
        "ftp"
    };

    @Override
    public GenerationResult generate(ObjectQuery query, ObjectGenerationContext context) {
        return query.getType().equals(URL.class)
            ? GenerationResult.presence(generate())
            : GenerationResult.absence();
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
