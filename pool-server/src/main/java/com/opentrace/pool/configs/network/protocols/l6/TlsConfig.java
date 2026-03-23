package com.opentrace.pool.configs.network.protocols.l6;

import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.PemKeyCertOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TlsConfig {

    private static final Logger logger = LoggerFactory.getLogger(TlsConfig.class);

    public static NetServerOptions applyTls(NetServerOptions options, JsonObject tlsOptions) {
        if (tlsOptions != null && tlsOptions.getBoolean("enabled", false)) {
            options.setSsl(true)
                    .setPemKeyCertOptions(new PemKeyCertOptions()
                            .setCertPath(tlsOptions.getString("certPath"))
                            .setKeyPath(tlsOptions.getString("keyPath")));

            logger.info("TLS integration: enabled. Using certs: {}", tlsOptions.getString("certPath"));
        } else {
            logger.warn("TLS integration: disabled. Server will use plain TCP.");
        }
        return options;
    }
}