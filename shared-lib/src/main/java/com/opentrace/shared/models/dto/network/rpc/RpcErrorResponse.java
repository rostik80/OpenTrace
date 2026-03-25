package com.opentrace.shared.models.dto.network.rpc;

import io.vertx.core.json.JsonObject;

public record RpcErrorResponse(
        RpcError error,
        Object id
) implements RpcMessage {

    public record RpcError(
            int code,
            String message,
            JsonObject data
    ) {}
}