package com.nextplugins.cash.command.registry;

import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.command.CashCommand;
import com.nextplugins.cash.configuration.MessageValue;
import com.nextplugins.cash.util.text.NumberUtil;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import me.saiintbrisson.minecraft.command.message.MessageType;

@RequiredArgsConstructor(staticName = "of")
public final class CommandRegistry {

    private final NextCash plugin;

    public void register() {
        try {
            final BukkitFrame frame = new BukkitFrame(plugin);

            frame.registerAdapter(Double.TYPE, argument -> {
                double value = Double.parseDouble(argument);

                if (NumberUtil.isInvalid(value)) return 0D;
                else return value;
            });

            frame.registerCommands(
                new CashCommand(
                    plugin,
                    plugin.getAccountStorage(),
                    plugin.getRankingStorage(),
                    plugin.getLocationManager()
                )
            );

            final MessageHolder messageHolder = frame.getMessageHolder();

            messageHolder.setMessage(MessageType.ERROR, MessageValue.get(MessageValue::error));
            messageHolder.setMessage(MessageType.INCORRECT_TARGET, MessageValue.get(MessageValue::incorrectTarget));
            messageHolder.setMessage(MessageType.INCORRECT_USAGE, MessageValue.get(MessageValue::incorrectUsage));
            messageHolder.setMessage(MessageType.NO_PERMISSION, MessageValue.get(MessageValue::noPermission));

            plugin.getTextLogger().info("Comandos registrados com sucesso.");
        } catch (Throwable t) {
            t.printStackTrace();
            plugin.getTextLogger().error("Não foi possível registrar os comandos.");
        }
    }

}
