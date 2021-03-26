package org.javaunit.autoparams;

import java.util.concurrent.ThreadLocalRandom;

final class Factories {

    private static ThreadLocalRandom random() {
        return ThreadLocalRandom.current();
    }

    public static boolean createBoolean() {
        return random().nextInt() % 2 == 0;
    }

    public static byte createByte() {
        return (byte) random().nextInt(Byte.MIN_VALUE, Byte.MAX_VALUE + 1);
    }

    public static short createShort() {
        return (short) random().nextInt(Short.MIN_VALUE, Short.MAX_VALUE + 1);
    }

    public static int createInt() {
        return random().nextInt();
    }

    public static long createLong() {
        return random().nextLong();
    }

    public static float createFloat() {
        return random().nextFloat();
    }

    public static double createDouble() {
        return random().nextDouble();
    }

    public static char createChar() {
        return (char) random().nextInt(Character.MAX_VALUE + 1);
    }

}
