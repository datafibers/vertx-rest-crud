package com.tymoshenko.controller.verticle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tymoshenko.controller.service.CrudService;
import com.tymoshenko.controller.service.WhiskyCrudService;
import com.tymoshenko.model.Whisky;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/** *
 * Simple verticle to wrap a Spring service bean - note we wrap the service call
 * in executeBlocking, because we know it's going to be a JDBC call which blocks.
 * As a general principle with Spring beans, the default assumption should be that it will block unless you
 * know for sure otherwise (in other words use executeBlocking unless you know for sure your service call will be
 * extremely quick to respond).
 *
 * @author Yakiv Tymoshenko
 * @since 15.03.2016
 */
public class EventBusVerticle extends AbstractVerticle {

    public static final String WHISKY_MESSAGE_CONSUMER_ADDRESS = "com.tymoshenko.whisky";
    private static final Logger LOG = LoggerFactory.getLogger(EventBusVerticle.class);

    private final ObjectMapper mapper = new ObjectMapper();
    private final CrudService<Whisky> whiskyCrudService;

    /**
     * Constructor that injects Beans from Spring Context
     *
     * @param context Spring ApplicationContext
     */
    public EventBusVerticle(final ApplicationContext context) {
        whiskyCrudService = (WhiskyCrudService) context.getBean("whiskyCrudService");
    }


    /**
     * Starts Verticle which provides EventBuss service.
     * Registers a message consumer and message handler for it.
     *
     * @throws Exception
     */
    @Override
    public void start() throws Exception {
        super.start();
        vertx.eventBus()
                .<String>consumer(WHISKY_MESSAGE_CONSUMER_ADDRESS)
                .handler(readAllWhiskyFromDBHandler());
    }

    private Handler<Message<String>> readAllWhiskyFromDBHandler() {
        return msg -> vertx.<String>executeBlocking(
                future -> {
                    try {
                        future.complete(mapper.writeValueAsString(whiskyCrudService.readAll()));
                    } catch (JsonProcessingException e) {
                        LOG.error("Failed to serialize result. ", e.getMessage());
                        future.fail(e);
                    }
                },
                result -> {
                    if (result.succeeded()) {
                        msg.reply(result.result());
                    } else {
                        msg.reply(result.cause().toString());
                    }
                }
        );
    }

}
