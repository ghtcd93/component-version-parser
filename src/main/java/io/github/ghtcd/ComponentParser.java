package io.github.ghtcd;

import io.github.ghtcd.internal.ComponentVersionParser;
import io.github.ghtcd.internal.PackageURLParser;
import java.util.Optional;

/**
 * A unified parser that intelligently delegates to the appropriate parser
 * based on the input string format (PURL or simple component string).
 */
public class ComponentParser {

    private static final String PURL_SCHEME = "pkg:";

    /**
     * Parses a component string, automatically detecting if it is a Package URL (PURL)
     * or a simple component string.
     *
     * @param componentString The string to parse.
     * @return An {@link Optional} containing the {@link ParseResult} if parsing is successful,
     *         otherwise an empty {@link Optional}.
     */
    public static Optional<ParseResult> parse(String componentString) {
        if (componentString != null && componentString.trim().toLowerCase().startsWith(PURL_SCHEME)) {
            return PackageURLParser.parse(componentString).map(ParseResult::from);
        } else {
            return ComponentVersionParser.parse(componentString).map(ParseResult::from);
        }
    }
}