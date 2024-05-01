package autoparams.generator;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class URLGenerator extends ObjectGeneratorBase<URL> {

    @Override
    protected URL generateObject(ObjectQuery query, ResolutionContext context) {
        URI uri = context.resolve(URI.class);
        try {
            return uri.toURL();
        } catch (MalformedURLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
