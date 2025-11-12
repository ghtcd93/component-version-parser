package io.github.ghtcd;

import java.util.regex.Pattern;
import java.util.Optional; 

public class ComponentVersionParser {

    /**
     * A simpler regex to identify if a string segment looks like a version.
     * It must start with a digit or the letter 'v'.
     */
    private static final Pattern VERSION_LIKE_PATTERN = Pattern.compile("^v?\\d.*");

    /**
     * Parses a component string into its name and version.
     *
     * @param componentString The full component string (e.g., "spring-boot-2.7.5").
     * @return An {@link Optional} containing the {@link ComponentVersion} if parsing is successful,
     *         otherwise an empty {@link Optional}.
     */
    public static Optional<ComponentVersion> parse(String componentString) {

        // if componentString is null or is Empty, return an empty
        if (componentString == null || componentString.trim().isEmpty()) {
            return Optional.empty();
        }

        // Iterate backwards from the end of the string to find the last separator ('-' or '@')
        for (int i = componentString.length() - 1; i > 0; i--) {
            char c = componentString.charAt(i);

            if (c == '-' || c == '@') {
                if (i > 0) {
                    String potentialVersion = componentString.substring(i + 1);
                    if (VERSION_LIKE_PATTERN.matcher(potentialVersion).matches()) {
                        String name = componentString.substring(0, i);
                        return Optional.of(new ComponentVersion(name, potentialVersion));
                    }
                }
            }
        }

        return Optional.empty();
    }
}
