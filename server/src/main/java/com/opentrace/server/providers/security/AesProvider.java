package com.opentrace.server.providers.security;

import com.opentrace.server.utils.generators.AesGenerator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import com.opentrace.server.properties.AesProperties;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@RequiredArgsConstructor
public class AesProvider {

    private final AesGenerator cryptoGenerator;
    private final AesProperties aesProperties;

    private byte[] aesKey;
    private byte[] aesIv;

    @PostConstruct
    public void init() throws Exception {
        String keyStr = aesProperties.getKey();
        String ivStr = aesProperties.getIv();

        if (keyStr == null || keyStr.isBlank() || ivStr == null || ivStr.isBlank()) {
            this.aesKey = cryptoGenerator.generateAesKey();
            this.aesIv = cryptoGenerator.generateIv();
            printWarning();
        } else {
            this.aesKey = Base64.getDecoder().decode(keyStr);
            this.aesIv = Base64.getDecoder().decode(ivStr);
        }
    }

    private void printWarning() {
        System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.err.println("WARNING: AES Key or IV not found in properties!");
        System.err.println("A temporary key has been generated for this session.");
        System.err.println("Data encrypted now will be UNREADABLE after restart!");
        System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    public byte[] getKey() {
        return aesKey;
    }

    public byte[] getIv() {
        return aesIv;
    }
}