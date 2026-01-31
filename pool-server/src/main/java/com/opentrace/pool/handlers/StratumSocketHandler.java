package com.opentrace.pool.handlers;

import io.vertx.core.Handler;
import io.vertx.core.net.NetSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StratumSocketHandler implements Handler<NetSocket> {

    private static final Logger logger = LoggerFactory.getLogger(StratumSocketHandler.class);

    @Override
    public void handle(NetSocket socket) {
        logger.info("New worker connected from: {}", socket.remoteAddress());

        socket.handler(buffer -> {
            String data = buffer.toString().trim();
            logger.info("Received from {}: {}", socket.remoteAddress(), data);

            socket.write("{\"id\":null,\"result\":true,\"error\":null}\n");
        });

        socket.closeHandler(v -> {
            logger.info("Worker disconnected: {}", socket.remoteAddress());
        });

        socket.exceptionHandler(e -> {
            logger.error("Socket error for {}", socket.remoteAddress(), e);
            socket.close();
        });
    }
}