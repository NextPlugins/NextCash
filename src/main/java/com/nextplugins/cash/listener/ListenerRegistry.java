package com.nextplugins.cash.listener;

import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.listener.check.CheckInteractListener;
import com.nextplugins.cash.listener.operation.CashDepositListener;
import com.nextplugins.cash.listener.operation.CashSetListener;
import com.nextplugins.cash.listener.operation.CashWithdrawListener;
import com.nextplugins.cash.listener.operation.UserDisconnectListener;
import com.nextplugins.cash.listener.transaction.TransactionRequestListener;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;

@RequiredArgsConstructor(staticName = "of")
public final class ListenerRegistry {

    private final NextCash plugin;

    public void register() {
        try {
            val pluginManager = Bukkit.getPluginManager();

            // system

            pluginManager.registerEvents(
                    new UserDisconnectListener(plugin.getAccountStorage()),
                    plugin
            );

            // operations

            pluginManager.registerEvents(
                    new CashDepositListener(plugin.getAccountStorage()),
                    plugin
            );
            pluginManager.registerEvents(
                    new CashSetListener(plugin.getAccountStorage()),
                    plugin
            );
            pluginManager.registerEvents(
                    new CashWithdrawListener(plugin.getAccountStorage()),
                    plugin
            );

            // transactions

            pluginManager.registerEvents(
                    new TransactionRequestListener(plugin.getAccountStorage()),
                    plugin
            );

            // check

            pluginManager.registerEvents(
                    new CheckInteractListener(plugin.getAccountStorage()),
                    plugin
            );

            plugin.getTextLogger().info("Listeners registrados com sucesso.");
        } catch (Throwable t) {
            t.printStackTrace();
            plugin.getTextLogger().error("Não foi possível registrar os listeners!");
        }
    }

}
