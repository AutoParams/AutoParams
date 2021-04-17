package org.javaunit.autoparams.generator;

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
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        return query.getType().equals(URL.class)
            ? new ObjectContainer(generate())
            : ObjectContainer.EMPTY;
    }

    private URL generate() {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        int index = random.nextInt(PROTOCOLS.length);
        String protocol = PROTOCOLS[index];

        boolean hasPort = random.nextBoolean();

        String urlSource = hasPort
            ? String.format("%s://auto.params:%s", protocol, random.nextInt(0, 99999))
            : String.format("%s://auto.params", protocol);

        try {
            return new URL(urlSource);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
