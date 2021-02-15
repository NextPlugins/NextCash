package com.nextplugins.cash.listener.operation;

import com.nextplugins.cash.api.event.operations.CashDepositEvent;
import com.nextplugins.cash.api.model.account.Account;
import com.nextplugins.cash.configuration.MessageValue;
import com.nextplugins.cash.storage.AccountStorage;
import com.nextplugins.cash.util.NumberFormat;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public final class CashDepositListener implements Listener {

    private final AccountStorage accountStorage;

    @EventHandler
    public void onCashDeposit(CashDepositEvent event) {
        Player player = event.getPlayer();
        Player target = event.getTarget();
        double amount = event.getAmount();

        Account targetAccount = accountStorage.getByName(target.getName());

        targetAccount.depositAmount(amount);

        player.sendMessage(MessageValue.get(MessageValue::addAmount)
                .replace("$player", targetAccount.getOwner().getName())
                .replace("$amount", NumberFormat.format(targetAccount.getBalance()))
        );
    }

}
