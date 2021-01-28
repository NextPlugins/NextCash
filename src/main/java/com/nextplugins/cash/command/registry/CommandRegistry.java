package com.nextplugins.cash.command.registry;

import com.google.inject.Inject;
import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.command.CashCommand;
import me.saiintbrisson.bukkit.command.BukkitFrame;

public final class CommandRegistry {

    @Inject private static NextCash plugin;

    public static void register() {
        try {
            BukkitFrame bukkitFrame = new BukkitFrame(plugin);

            bukkitFrame.registerCommands(
                    new CashCommand(plugin.getAccountStorage(), plugin.getRankingStorage())
            );
        } catch (Throwable t) {
            t.printStackTrace();
            plugin.getLogger().severe("Não foi possível registrar os comandos.");
        }
    }

}
