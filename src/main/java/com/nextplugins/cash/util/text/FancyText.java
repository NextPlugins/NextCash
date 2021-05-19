package com.nextplugins.cash.util.text;

import lombok.Data;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

@Data
public final class FancyText {

    private final TextComponent textComponent;

    public FancyText(String message) {
        this.textComponent = new TextComponent(message);
    }

    public FancyText hover(HoverEvent.Action action, String... messages) {
        String message = String.join("\n", messages);

        this.textComponent.setHoverEvent(
                new HoverEvent(action, new ComponentBuilder(message).create())
        );

        return this;
    }

    public FancyText hover(HoverEvent.Action action, String message) {
        this.textComponent.setHoverEvent(
                new HoverEvent(action, new ComponentBuilder(message).create())
        );

        return this;
    }

    public FancyText click(ClickEvent.Action action, String message) {
        this.textComponent.setClickEvent(
                new ClickEvent(action, message)
        );

        return this;
    }

    public TextComponent build() {
        return this.textComponent;
    }

}
