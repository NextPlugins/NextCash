package com.nextplugins.cash.command.registry;

import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.command.CashCommand;
import lombok.Data;
import me.saiintbrisson.bukkit.command.BukkitFrame;

@Data(staticConstructor = "of")
public final class CommandRegistry {

    private final NextCash plugin;

    public void register() {
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
