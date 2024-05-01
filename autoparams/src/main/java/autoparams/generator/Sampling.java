package autoparams.generator;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

final class Sampling {

    public static <T> T sample(List<T> source) {
        return source.get(ThreadLocalRandom.current().nextInt(source.size()));
    }
}
