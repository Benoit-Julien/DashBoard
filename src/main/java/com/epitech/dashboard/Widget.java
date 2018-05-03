package com.epitech.dashboard;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Class to store a widget in the db
 */
public class Widget {
    @Id
    private String id;

    /**
     * Owner of this instantiated widget
     */
    private User owner = null;

    /**
     * Widget real classname
     */
    private String type;

    /**
     * Necessary data to re-instantiate a new widget
     */
    @Field("content_data")
    private Object instance;

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
