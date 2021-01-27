package com.nextplugins.cash.configuration;

import com.henryfabio.minecraft.configinjector.common.annotations.ConfigField;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigFile;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigSection;
import com.henryfabio.minecraft.configinjector.common.annotations.TranslateColors;
import com.henryfabio.minecraft.configinjector.common.injector.ConfigurationInjectable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.function.Function;

@Getter
@TranslateColors
@Accessors(fluent = true)
@ConfigSection("messages")
@ConfigFile("messages.yml")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageValue implements ConfigurationInjectable {

    @Getter private static final MessageValue instance = new MessageValue();

    // requests

    @ConfigField("requests.invalid-target") private String invalidTarget;

    // operations

    @ConfigField("operations.set") private String setAmount;
    @ConfigField("operations.add") private String addAmount;
    @ConfigField("operations.remove") private String removeAmount;
    @ConfigField("operations.reset") private String resetBalance;
    @ConfigField("operations.see") private String seeBalance;
    @ConfigField("operations.see-other") private String seeOtherBalance;

    public static <T> T get(Function<MessageValue, T> function) {
        return function.apply(instance);
    }

}
