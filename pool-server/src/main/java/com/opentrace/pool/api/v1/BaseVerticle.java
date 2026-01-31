package com.opentrace.pool.api.v1;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.net.NetServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseVerticle extends AbstractVerticle {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected void listenAndReport(NetServer server, int port, String host, Promise<Void> startPromise) {
        server.listen(port, host, res -> {
            if (res.succeeded()) {
                logger.info("{} started on {}:{}", this.getClass().getSimpleName(), host, port);
                startPromise.complete();
            } else {
                logger.error("Failed to start {} on port {}", this.getClass().getSimpleName(), port, res.cause());
                startPromise.fail(res.cause());
            }
        });
    }
}