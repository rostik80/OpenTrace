package com.opentrace.server.utils.codecs;

import org.springframework.stereotype.Component;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

@Component
public class GoogleStatePacker {

    private static final String DELIMITER = "|";

    public String encode(String roles, String publicKey) {
        if (roles == null) roles = "";
        if (publicKey == null) publicKey = "";

        String combined = roles + DELIMITER + publicKey;
        return Base64.getUrlEncoder().withoutPadding().encodeToString(combined.getBytes(StandardCharsets.UTF_8));
    }

    public StateData decode(String state) {
        if (state == null || state.isEmpty()) {
            return new StateData("", null);
        }

        try {
            byte[] decodedBytes = Base64.getUrlDecoder().decode(state.trim());
            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
            String[] parts = decodedString.split("\\|", 2);

            return new StateData(
                    parts[0],
                    parts.length > 1 ? parts[1] : null
            );
        } catch (IllegalArgumentException e) {
            return new StateData("", null);
        }
    }

    public record StateData(String roles, String publicKey) {}
}