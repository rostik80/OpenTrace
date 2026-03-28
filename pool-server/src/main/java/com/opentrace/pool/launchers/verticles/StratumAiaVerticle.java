package com.opentrace.pool.launchers.verticles;

import com.opentrace.pool.configs.network.protocols.l6.TlsConfig;
import com.opentrace.pool.handlers.network.protocols.l7.StratumAiaSocketHandler;
import com.opentrace.pool.routers.rpc.RpcRouter;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;

public class StratumAiaVerticle extends BaseVerticle {

    @Override
    public void start(Promise<Void> startPromise) {
        JsonObject stratumOptions = config().getJsonObject("stratum", new JsonObject());

        NetServerOptions serverOptions = new NetServerOptions();

        TlsConfig.applyTls(serverOptions, stratumOptions.getJsonObject("tls"));

        NetServer server = vertx.createNetServer(serverOptions);
        server.connectHandler(new StratumAiaSocketHandler(new RpcRouter()));

        listen(
                server,
                stratumOptions.getInteger("port", 3333),
                stratumOptions.getString("host", "0.0.0.0"),
                startPromise
        );
    }
}