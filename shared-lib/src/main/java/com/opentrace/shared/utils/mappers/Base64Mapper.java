package com.opentrace.shared.utils.mappers;

import org.springframework.stereotype.Component;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


@Component
public final class Base64Mapper {

    private Base64Mapper() {
    }

    public PublicKey toPublicKey(String publicKeyBase64) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(publicKeyBase64);

            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return keyFactory.generatePublic(spec);
        } catch (GeneralSecurityException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid RSA public key", e);
        }
    }

    public static byte[] toBytes(String base64) {
        if (base64 == null) {
            return null;
        }
        return Base64.getDecoder().decode(base64);
    }

    public static String toBase64(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(bytes);
    }
}
