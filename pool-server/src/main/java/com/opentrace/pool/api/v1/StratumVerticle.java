package com.opentrace.pool.api.v1;

import com.opentrace.pool.handlers.StratumSocketHandler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetServer;

public class StratumVerticle extends BaseVerticle {

    @Override
    public void start(Promise<Void> startPromise) {
        JsonObject stratumOpts = config().getJsonObject("stratum", new JsonObject());

        if (stratumOpts == null) {
            startPromise.fail("Configuration section 'stratum' is missing!");
            return;
        }

        NetServer server = vertx.createNetServer();
        server.connectHandler(new StratumSocketHandler());

        listenAndReport(
                server,
                stratumOpts.getInteger("port", 3333),
                stratumOpts.getString("host", "0.0.0.0"),
                startPromise
        );
    }
}