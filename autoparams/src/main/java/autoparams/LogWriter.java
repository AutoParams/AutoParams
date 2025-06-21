package autoparams;

/**
 * A functional interface for writing log messages during object resolution.
 * <p>
 * This interface defines a contract for outputting log messages generated
 * during the object resolution process. Implementations can direct log
 * output to various destinations such as console, files, or other logging
 * systems.
 * </p>
 */
@FunctionalInterface
public interface LogWriter {

    /**
     * Writes the specified log message.
     * <p>
     * Implementations should handle the given message appropriately,
     * such as writing to console, file, or other output destinations.
     * The message content and format are determined by the logging
     * system that calls this method.
     * </p>
     *
     * @param message the log message to write; may be null or empty
     */
    void write(String message);
}
