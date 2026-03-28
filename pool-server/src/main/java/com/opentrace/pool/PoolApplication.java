package com.opentrace.pool;

import com.opentrace.pool.launchers.verticles.StratumAiaVerticle;
import com.opentrace.pool.configs.ConfigRetrieverFactory;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.Vertx;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PoolApplication {

    private static final Logger logger = LoggerFactory.getLogger(PoolApplication.class);

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        ConfigRetriever configManager = ConfigRetrieverFactory.createRetriever(vertx);

        configManager.getConfig(arg -> {
            if (arg.succeeded()) {
                JsonObject config = arg.result();

                int instances = config.getJsonObject("stratum", new JsonObject())
                        .getInteger("instances", Runtime.getRuntime().availableProcessors());

                DeploymentOptions options = new DeploymentOptions()
                        .setInstances(instances)
                        .setConfig(config);

                vertx.deployVerticle(StratumAiaVerticle.class.getName(), options, res -> {
                    if (res.succeeded()) {
                        logger.info("Pool Server successfully started with {} instances", instances);
                    } else {
                        logger.error("Failed to deploy StratumVerticle", res.cause());
                        vertx.close();
                    }
                });
            } else {
                logger.error("Failed to load configuration", arg.cause());
                vertx.close();
            }
        });
    }
}