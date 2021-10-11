package com.nextplugins.cash.command.registry;

import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.command.CashCommand;
import com.nextplugins.cash.configuration.MessageValue;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import me.saiintbrisson.minecraft.command.message.MessageType;

@RequiredArgsConstructor(staticName = "of")
public final class CommandRegistry {

    private final NextCash plugin;

    public void register() {
        try {
            BukkitFrame bukkitFrame = new BukkitFrame(plugin);

            bukkitFrame.registerCommands(
                new CashCommand(
                    plugin,
                    plugin.getAccountStorage(),
                    plugin.getRankingStorage(),
                    plugin.getLocationManager()
                )
            );

            MessageHolder messageHolder = bukkitFrame.getMessageHolder();

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
