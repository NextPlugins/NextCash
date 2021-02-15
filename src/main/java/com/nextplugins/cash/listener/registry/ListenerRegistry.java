package com.nextplugins.cash.listener.registry;

import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.listener.UserConnectListener;
import com.nextplugins.cash.listener.UserDisconnectListener;
import com.nextplugins.cash.listener.operation.CashDepositListener;
import com.nextplugins.cash.listener.operation.CashSetListener;
import com.nextplugins.cash.listener.operation.CashWithdrawListener;
import com.nextplugins.cash.listener.transaction.TransactionRequestListener;
import lombok.Data;
import org.bukkit.Bukkit;

@Data(staticConstructor = "of")
public final class ListenerRegistry {

    private final NextCash plugin;

    public void register() {
        try {
            // system

            Bukkit.getPluginManager().registerEvents(
                    new UserConnectListener(plugin.getAccountStorage()),
                    plugin
            );
            Bukkit.getPluginManager().registerEvents(
                    new UserDisconnectListener(plugin.getAccountStorage()),
                    plugin
            );

            // operations

            Bukkit.getPluginManager().registerEvents(
                    new CashDepositListener(plugin.getAccountStorage()),
                    plugin
            );
            Bukkit.getPluginManager().registerEvents(
                    new CashSetListener(plugin.getAccountStorage()),
                    plugin
            );
            Bukkit.getPluginManager().registerEvents(
                    new CashWithdrawListener(plugin.getAccountStorage()),
                    plugin
            );

            // transactions

            Bukkit.getPluginManager().registerEvents(
                    new TransactionRequestListener(plugin.getAccountStorage()),
                    plugin
            );

            plugin.getLogger().info("Listeners registrados com sucesso.");
        } catch (Throwable t) {
            t.printStackTrace();
            plugin.getLogger().severe("Não foi possível registrar os listeners!");
        }
    }

}
