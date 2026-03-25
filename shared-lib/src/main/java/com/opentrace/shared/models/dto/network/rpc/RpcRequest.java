package com.opentrace.shared.models.dto.network.rpc;

import io.vertx.core.json.JsonObject;

public record RpcRequest(
        String method,
        JsonObject params,
        Object id
) implements RpcMessage {}