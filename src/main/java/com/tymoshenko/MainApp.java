package com.tymoshenko;

import com.tymoshenko.controller.context.SpringConfig;
import com.tymoshenko.controller.verticle.HttpServerVerticle;
import io.vertx.core.Vertx;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Runner class for the application.
 *
 * Deploys a verticle which runs an HTTP Server into Vertx.
 * We can execute REST methods for CRUD against the HTTP Server Verticle.
 *
 * @author Yakiv Tymoshenko
 * @since 15.03.2016
 */
public class MainApp {

    public static void main(String[] args) {
        final ApplicationContext _context = new AnnotationConfigApplicationContext(SpringConfig.class);
        final HttpServerVerticle _httpServerVerticle = new HttpServerVerticle(_context);
        final Vertx _vertx = Vertx.vertx();

        _vertx.deployVerticle(_httpServerVerticle);
    }
}
