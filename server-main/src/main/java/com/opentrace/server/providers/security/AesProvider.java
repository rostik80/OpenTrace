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

    private byte[] aesIv;

    @PostConstruct
    public void init() throws Exception {
        String ivStr = aesProperties.getIv();

        if (ivStr == null || ivStr.isBlank()) {
            this.aesIv = cryptoGenerator.generateIv();
            printWarning();
        } else {
            this.aesIv = Base64.getDecoder().decode(ivStr);
        }
    }

    private void printWarning() {
        System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.err.println("IV not found in properties!");
        System.err.println("A temporary key has been generated for this session.");
        System.err.println("Data encrypted now will be UNREADABLE after restart!");
        System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }


    public byte[] getIv() {
        return aesIv;
    }
}