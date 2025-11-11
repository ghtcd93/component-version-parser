package io.github.ghtcd;

import java.util.regex.Pattern;
import java.util.Optional;
import java.util.StringJoiner;

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

        // if componentString is null or empty, return empty Optional
        if (componentString == null || componentString.trim().isEmpty()) {
            return Optional.empty();
        }

        // if componentString doesn't contain a hyphen, return empty Optional
        String[] parts = componentString.split("-");
        if (parts.length < 2) {
            return Optional.empty();
        }

        for (int i = parts.length - 1; i > 0; i--) {
            String potentialVersion = parts[i];
            if (VERSION_LIKE_PATTERN.matcher(potentialVersion).matches()) {
                StringJoiner nameJoiner = new StringJoiner("-");
                for (int j = 0; j < i; j++) {
                    nameJoiner.add(parts[j]);
                }
                String name = nameJoiner.toString();
                String version = componentString.substring(name.length() + 1);
                return Optional.of(new ComponentVersion(name, version));
            }
        }

        return Optional.empty();
    }
}
