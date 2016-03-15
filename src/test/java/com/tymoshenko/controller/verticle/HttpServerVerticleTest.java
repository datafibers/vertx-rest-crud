package com.tymoshenko.controller.verticle;

import com.tymoshenko.controller.context.SpringConfig;
import com.tymoshenko.model.Whisky;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author Yakiv
 * @since 15.03.2016
 */
@RunWith(VertxUnitRunner.class)
public class HttpServerVerticleTest {

    private Vertx vertx;
    private Integer port;

    /**
     * Before executing our test, let's deploy our verticle.
     * <p>
     * This method instantiates a new Vertx and deploy the verticle. Then, it waits in the verticle has successfully
     * completed its start sequence (thanks to `context.asyncAssertSuccess`).
     *
     * @param context the test context.
     */
    @Before
    public void setUp(TestContext context) throws IOException {
        vertx = Vertx.vertx();

        // Let's configure the verticle to listen on the 'test' port (randomly picked).
        // We create deployment options and set the _configuration_ json object:
        ServerSocket socket = new ServerSocket(0);
        port = socket.getLocalPort();
        socket.close();

        DeploymentOptions options = new DeploymentOptions()
                .setConfig(new JsonObject().put("http.port", port)
                );


        final ApplicationContext _context = new AnnotationConfigApplicationContext(SpringConfig.class);
        final HttpServerVerticle _httpServerVerticle = new HttpServerVerticle(_context);
        // We pass the options as the second parameter of the deployVerticle method.
        vertx.deployVerticle(_httpServerVerticle, options, context.asyncAssertSuccess());
    }

    /**
     * This method, called after our test, just cleanup everything by closing the vert.x instance
     *
     * @param context the test context
     */
    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testHttpServer_IsStarted_AndDisplaysHelloMessage(TestContext context) {
        // This test is asynchronous, so get an async handler to inform the test when we are done.
        final Async async = context.async();

        // We create a HTTP client and query our application. When we get the response we check it contains the 'Hello'
        // message. Then, we call the `complete` method on the async handler to declare this async (and here the test) done.
        // Notice that the assertions are made on the 'context' object and are not Junit assert. This ways it manage the
        // async aspect of the test the right way.
        vertx.createHttpClient().getNow(port, "localhost", "/", response -> {
            response.handler(body -> {
                context.assertTrue(body.toString().contains("Hello"));
                async.complete();
            });
        });
    }

    @Test
    public void testHttpPost_CanCreateNewWhiskyEntity(TestContext context) {
        Async async = context.async();
        final String json = Json.encodePrettily(new Whisky("Jameson", "Ireland"));
        vertx.createHttpClient().post(port, "localhost", WhiskyCrudRestService.REST_WHISKYS_URL)
                .putHeader(WhiskyCrudRestService.CONTENT_TYPE, WhiskyCrudRestService.APPLICATION_JSON_CHARSET_UTF_8)
                .putHeader("content-length", Integer.toString(json.length()))
                .handler(response -> {
                    context.assertEquals(response.statusCode(), WhiskyCrudRestService.STATUS_CODE_OK_CREATED);
                    context.assertTrue(response.headers().get(WhiskyCrudRestService.CONTENT_TYPE).contains(WhiskyCrudRestService.APPLICATION_JSON_CHARSET_UTF_8));
                    response.bodyHandler(body -> {
                        final Whisky whisky = Json.decodeValue(body.toString(), Whisky.class);
                        context.assertEquals(whisky.getName(), "Jameson");
                        context.assertEquals(whisky.getOrigin(), "Ireland");
                        context.assertNotNull(whisky.getId());
                        async.complete();
                    });
                })
                .write(json)
                .end();
    }

}