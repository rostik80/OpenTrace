package com.opentrace.shared.models.dto.network.rpc;

public record RpcResponse(
        Object result,
        Object id
) implements RpcMessage {}