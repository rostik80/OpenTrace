package com.opentrace.pool.configs;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class ConfigManager {

    public static ConfigRetriever createRetriever(Vertx vertx) {
        ConfigStoreOptions fileStore = new ConfigStoreOptions()
                .setType("file")
                .setOptional(true)
                .setConfig(new JsonObject().put("path", "config.json"));


        ConfigRetrieverOptions options = new ConfigRetrieverOptions()
                .addStore(fileStore);

        return ConfigRetriever.create(vertx, options);
    }
}