package com.nextplugins.cash.listener.operation;

import com.nextplugins.cash.api.event.operations.CashDepositEvent;
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
public final class CashDepositListener implements Listener {

    private final AccountStorage accountStorage;

    @EventHandler
    public void onCashDeposit(CashDepositEvent event) {
        CommandSender sender = event.getSender();
        Player target = event.getTarget();
        double amount = event.getAmount();

        Account targetAccount = accountStorage.getByName(target.getName());

        targetAccount.depositAmount(amount);

        sender.sendMessage(MessageValue.get(MessageValue::addAmount)
                .replace("$player", targetAccount.getOwner().getName())
                .replace("$amount", NumberFormat.format(targetAccount.getBalance()))
        );
    }

}
