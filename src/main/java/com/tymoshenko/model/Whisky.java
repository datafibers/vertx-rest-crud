package com.tymoshenko.model;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Yakiv Tymoshenko
 * @since 07.03.2016
 */
public class Whisky {

    private static final AtomicInteger COUNTER = new AtomicInteger();

    private final int id;

    private String name;

    private String origin;

    public Whisky() {
        this.id = COUNTER.getAndIncrement();
    }

    public Whisky(String name, String origin) {
        this.id = COUNTER.getAndIncrement();
        this.name = name;
        this.origin = origin;
    }


    public String getName() {
        return name;
    }

    public String getOrigin() {
        return origin;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
