package org.javaunit.autoparams;

import java.util.concurrent.ThreadLocalRandom;

final class Factories {

    static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    public static boolean createBoolean() {
        return RANDOM.nextInt() % 2 == 0;
    }

    public static byte createByte() {
        return (byte) RANDOM.nextInt(Byte.MIN_VALUE, Byte.MAX_VALUE + 1);
    }

}
