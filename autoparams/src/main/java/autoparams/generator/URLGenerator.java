package autoparams.generator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

import static java.lang.String.format;

final class URLGenerator extends ObjectGeneratorBase<URL> {

    private static final String[] PROTOCOLS = new String[] {
        "http",
        "https",
        "ftp"
    };

    @Override
    protected URL generateObject(ObjectQuery query, ResolutionContext context) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        int index = random.nextInt(PROTOCOLS.length);
        String protocol = PROTOCOLS[index];

        boolean hasPort = random.nextBoolean();

        String urlSource = hasPort
            ? format("%s://auto.params:%s", protocol, random.nextInt(0, 99999))
            : format("%s://auto.params", protocol);

        try {
            return new URL(urlSource);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
