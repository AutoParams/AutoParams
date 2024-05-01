package autoparams.generator;

import java.net.URI;
import java.util.UUID;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

import static autoparams.generator.Sampling.sample;
import static java.lang.String.format;

final class URIGenerator extends ObjectGeneratorBase<URI> {

    @Override
    protected URI generateObject(ObjectQuery query, ResolutionContext context) {
        URIGenerationOptions options = getOptions(context);

        String protocol = getProtocol(options);
        String host = getHost(options);
        Integer port = getPort(options);
        String path = getPath();

        String source = port == null
            ? format("%s://%s%s", protocol, host, path)
            : format("%s://%s:%s%s", protocol, host, port, path);

        return URI.create(source);
    }

    private static URIGenerationOptions getOptions(ResolutionContext context) {
        return context.resolve(URIGenerationOptions.class);
    }

    private String getProtocol(URIGenerationOptions options) {
        return sample(options.schemes());
    }

    private String getHost(URIGenerationOptions options) {
        return sample(options.hosts());
    }

    private Integer getPort(URIGenerationOptions options) {
        return options.ports().isEmpty() ? null : sample(options.ports());
    }

    private String getPath() {
        return "/" + UUID.randomUUID();
    }
}
