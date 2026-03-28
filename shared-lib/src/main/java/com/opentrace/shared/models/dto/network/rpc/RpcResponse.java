package com.opentrace.shared.models.dto.network.rpc;

import lombok.Builder;

@Builder
public record RpcResponse(
        Object result,
        Object id
) implements RpcMessage {}