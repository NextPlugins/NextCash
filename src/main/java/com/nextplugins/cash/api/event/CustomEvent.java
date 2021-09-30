package com.nextplugins.cash.api.event;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class CustomEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    protected CustomEvent(boolean isAsync) {
        super(isAsync);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}
