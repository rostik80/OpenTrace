package com.opentrace.shared.crypto;

import com.opentrace.shared.utils.mappers.Base64Mapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;

@AllArgsConstructor
@Component
public class RsaCipher {

    private final Base64Mapper base64Mapper;

    private static final String ALGORITHM =
            "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";

    public byte[] encrypt(byte[] data, String publicKeyBase64) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, base64Mapper.toPublicKey(publicKeyBase64));
            return cipher.doFinal(data);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("RSA encryption failed", e);
        }
    }
}
