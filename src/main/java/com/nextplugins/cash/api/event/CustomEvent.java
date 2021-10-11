package com.nextplugins.cash.api.event;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class CustomEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    protected CustomEvent(boolean isAsync) {
        super(isAsync);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

}
