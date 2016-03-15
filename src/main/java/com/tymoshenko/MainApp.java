package com.tymoshenko;

import com.tymoshenko.controller.context.SpringConfig;
import com.tymoshenko.controller.verticle.HttpServerVerticle;
import com.tymoshenko.controller.verticle.EventBusVerticle;
import io.vertx.core.Vertx;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Runner class
 *
 * @author Yakiv Tymoshenko
 * @since 15.03.2016
 */
public class MainApp {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        final Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new EventBusVerticle(context));
        vertx.deployVerticle(new HttpServerVerticle());
    }
}
