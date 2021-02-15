package com.nextplugins.cash.listener.transaction;

import com.nextplugins.cash.api.event.transactions.TransactionCompletedEvent;
import com.nextplugins.cash.api.event.transactions.TransactionRequestEvent;
import com.nextplugins.cash.api.model.account.Account;
import com.nextplugins.cash.configuration.MessageValue;
import com.nextplugins.cash.storage.AccountStorage;
import com.nextplugins.cash.util.NumberFormat;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public final class TransactionRequestListener implements Listener {

    private final AccountStorage accountStorage;

    @EventHandler
    public void onTransactionRequest(TransactionRequestEvent event) {
        final Player player = event.getPlayer();
        final Player target = event.getTarget();
        final double amount = event.getAmount();

        Account account = accountStorage.getByName(player.getName());
        Account targetAccount = accountStorage.getByName(target.getName());

        if (account.hasAmount(amount)) {
            targetAccount.depositAmount(amount);
            account.withdrawAmount(amount);

            player.sendMessage(
                    MessageValue.get(MessageValue::paid).replace("$player", target.getName())
                            .replace("$amount", NumberFormat.format(amount))
            );

            target.sendMessage(
                    MessageValue.get(MessageValue::received).replace("$player", player.getName())
                            .replace("$amount", NumberFormat.format(amount))
            );

            Bukkit.getPluginManager().callEvent(new TransactionCompletedEvent(player, target, amount));
        } else {
            event.setCancelled(true);
            player.sendMessage(MessageValue.get(MessageValue::insufficientAmount));
        }
    }

}
