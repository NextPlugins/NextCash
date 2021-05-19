package com.nextplugins.cash.listener.operation;

import com.nextplugins.cash.api.event.operations.CashSetEvent;
import com.nextplugins.cash.api.model.account.Account;
import com.nextplugins.cash.configuration.MessageValue;
import com.nextplugins.cash.storage.AccountStorage;
import com.nextplugins.cash.util.text.NumberFormat;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public final class CashSetListener implements Listener {

    private final AccountStorage accountStorage;

    @EventHandler
    public void onCashSet(CashSetEvent event) {
        final CommandSender sender = event.getSender();
        final Player target = event.getTarget();
        final double amount = event.getAmount();

        final Account targetAccount = accountStorage.getByName(target.getName());

        targetAccount.setBalance(amount);

        sender.sendMessage(MessageValue.get(MessageValue::setAmount)
                .replace("$player", targetAccount.getOwner().getName())
                .replace("$amount", NumberFormat.format(targetAccount.getBalance()))
        );
    }

}
