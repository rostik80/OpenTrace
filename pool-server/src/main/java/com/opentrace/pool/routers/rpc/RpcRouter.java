package com.opentrace.pool.routers.rpc;

import com.opentrace.shared.models.dto.network.rpc.RpcRequest;
import io.vertx.core.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class RpcRouter {
    private static final Logger logger = LoggerFactory.getLogger(RpcRouter.class);
    private final Map<String, Function<RpcRequest, Future<Object>>> routes = new HashMap<>();

    public void register(String method, Function<RpcRequest, Future<Object>> handler) {
        routes.put(method, handler);
        logger.info("Registered RPC route: {}", method);
    }

    public Future<Object> route(RpcRequest request) {
        var handler = routes.get(request.method());
        if (handler == null) {
            return Future.failedFuture("Method not found: " + request.method());
        }
        return handler.apply(request);
    }
}