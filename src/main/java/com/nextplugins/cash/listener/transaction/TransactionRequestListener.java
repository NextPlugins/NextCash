package com.nextplugins.cash.listener.transaction;

import com.nextplugins.cash.api.event.transactions.TransactionCompletedEvent;
import com.nextplugins.cash.api.event.transactions.TransactionRequestEvent;
import com.nextplugins.cash.configuration.MessageValue;
import com.nextplugins.cash.storage.AccountStorage;
import com.nextplugins.cash.util.text.NumberFormat;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public final class TransactionRequestListener implements Listener {

    private final AccountStorage accountStorage;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTransactionRequest(TransactionRequestEvent event) {

        if (event.isCancelled()) return;

        val player = event.getPlayer();
        val target = event.getTarget();
        val amount = event.getAmount();

        val account = accountStorage.findAccount(player);
        val targetAccount = accountStorage.findAccount(target);
        if (targetAccount == null) {

            event.setCancelled(true);
            player.sendMessage(MessageValue.get(MessageValue::invalidTarget));
            return;

        }

        if (!targetAccount.isReceiveCash()) {
            event.setCancelled(true);

            player.sendMessage(MessageValue.get(MessageValue::toggledOff)
                    .replace("$player", target.getName())
            );

            return;
        }

        if (account.hasAmount(amount)) {
            targetAccount.depositAmount(amount);
            account.withdrawAmount(amount);

            player.sendMessage(
                    MessageValue.get(MessageValue::paid).replace("$player", target.getName())
                            .replace("$amount", NumberFormat.format(amount))
            );

            if (target.isOnline()) target.getPlayer().sendMessage(
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
