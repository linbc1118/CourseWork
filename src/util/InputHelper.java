package util;

import java.util.Scanner;

/**
 * Utility class for safe console input with validation.
 * Reuses a single static Scanner instance for all input operations.
 *
 * All methods loop until valid input is received, printing
 * error messages for invalid entries.
 */
public class InputHelper {

    /** Shared Scanner for all input. Created once and reused. */
    private static final Scanner scanner = new Scanner(System.in);

    // ========== Integer Input ==========

    /**
     * Reads a valid integer from the console.
     * Prints the given prompt, then loops until a valid integer is entered.
     *
     * @param prompt the message to display before reading
     * @return the integer value entered
     */
    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    /**
     * Reads an integer within a specified range [min, max] inclusive.
     * Loops until a valid integer in range is entered.
     *
     * @param prompt the message to display before reading
     * @param min    minimum allowed value (inclusive)
     * @param max    maximum allowed value (inclusive)
     * @return the integer value entered (guaranteed min <= value <= max)
     */
    public static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Invalid range. Please enter a number between "
                    + min + " and " + max + ".");
        }
    }

    // ========== String Input ==========

    /**
     * Reads a string from the console.
     * Leading and trailing whitespace is removed.
     *
     * @param prompt the message to display before reading
     * @return the trimmed string (may be empty)
     */
    public static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Reads a non-empty string from the console.
     * Loops until a non-empty (after trimming) string is entered.
     *
     * @param prompt the message to display before reading
     * @return the trimmed non-empty string
     */
    public static String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
    }
}
