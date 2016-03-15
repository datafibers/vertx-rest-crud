package com.tymoshenko.controller.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;

/**
 * Simple web server verticle to expose the results of the Spring service bean call
 * (routed via a verticle - see EventBusVerticle)
 *
 * @author Yakiv Tymoshenko
 * @since 15.03.2016
 */
public class HttpServerVerticle extends AbstractVerticle {

    /**
     * Starts an HTTP server and
     * registers REST methods.
     *
     * @throws Exception
     */
    @Override
    public void start() throws Exception {
        super.start();
        HttpServer server = vertx.createHttpServer();
        server.requestHandler(req -> {
            if (req.method() == HttpMethod.GET) {
                req.response()
                        .setChunked(true)
                        .putHeader("Accept", "application/json")
                        .putHeader("Content-type", "application/json")
                ;

                if (req.path().equals("/whiskys")) {
                    handleGetAll(req);
                } else {
                    req.response().setStatusCode(200).write("Hello from vert.x").end();
                }

            } else {
                // We only support GET for now
                req.response().setStatusCode(405).end();
            }
        });

        server.listen(8080);
    }

    private void handleGetAll(HttpServerRequest req) {
        vertx.eventBus().<String>send(EventBusVerticle.WHISKY_MESSAGE_CONSUMER_ADDRESS, "", result -> {
            if (result.succeeded()) {
                req.response().setStatusCode(200).write(result.result().body()).end();
            } else {
                req.response().setStatusCode(500).write(result.cause().toString()).end();
            }
        });
    }
}
