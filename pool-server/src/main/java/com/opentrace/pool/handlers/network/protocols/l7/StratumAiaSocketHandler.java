package com.opentrace.pool.handlers.network.protocols.l7;

import com.opentrace.pool.routers.rpc.RpcRouter;
import com.opentrace.shared.models.dto.network.rpc.RpcRequest;
import com.opentrace.shared.models.dto.network.rpc.RpcResponse;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.net.NetSocket;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class StratumAiaSocketHandler implements Handler<NetSocket> {

    private static final Logger logger = LoggerFactory.getLogger(StratumAiaSocketHandler.class);
    private final RpcRouter rpcRouter;

    @Override
    public void handle(NetSocket socket) {
        socket.handler(buffer -> {
            try {
                String data = buffer.toString().trim();

                RpcRequest request = Json.decodeValue(data, RpcRequest.class);

                rpcRouter.route(request)
                        .onSuccess(result -> sendResponse(socket, request.id(), result))
                        .onFailure(err -> sendError(socket, request.id(), err.getMessage()));

            } catch (Exception e) {
                logger.error("Failed to parse RPC request from {}", socket.remoteAddress(), e);
                sendError(socket, null, "Invalid JSON-RPC request");
            }
        });

        socket.closeHandler(v -> logger.info("Worker disconnected: {}", socket.remoteAddress()));
    }

    private void sendResponse(NetSocket socket, Object id, Object result) {
        RpcResponse response = RpcResponse.builder()
                .id(id)
                .result(result)
                .build();
        socket.write(Json.encode(response) + "\n");
    }

    private void sendError(NetSocket socket, Object id, String message) {
        socket.write("{\"id\":" + id + ",\"result\":null,\"error\":\"" + message + "\"}\n");
    }
}