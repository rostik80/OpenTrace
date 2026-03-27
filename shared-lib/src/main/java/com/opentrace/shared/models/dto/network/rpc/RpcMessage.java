package com.opentrace.shared.models.dto.network.rpc;

public interface RpcMessage {
    String jsonrpc = "2.0";
    Object id();
}