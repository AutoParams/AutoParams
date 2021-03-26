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

    public static short createShort() {
        return (short) RANDOM.nextInt(Short.MIN_VALUE, Short.MAX_VALUE + 1);
    }

    public static int createInt() {
        return RANDOM.nextInt();
    }

    public static long createLong() {
        return RANDOM.nextLong();
    }

    public static float createFloat() {
        return RANDOM.nextFloat();
    }

    public static double createDouble() {
        return RANDOM.nextDouble();
    }

    public static char createChar() {
        return (char) RANDOM.nextInt(Character.MAX_VALUE + 1);
    }

}
