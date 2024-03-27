package test.autoparams.customization;

public class User extends Entity<Long> {

    @SuppressWarnings({ "FieldCanBeLocal", "FieldMayBeFinal" })
    private static String defaultGreeting = "hello world";

    private String username;

    public static String getDefaultGreeting() {
        return defaultGreeting;
    }

    public String getUsername() {
        return username;
    }
}
