package com.nextplugins.cash.listener.operation;

import com.nextplugins.cash.api.event.operations.CashDepositEvent;
import com.nextplugins.cash.configuration.MessageValue;
import com.nextplugins.cash.storage.AccountStorage;
import com.nextplugins.cash.util.text.NumberUtil;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public final class CashDepositListener implements Listener {

    private final AccountStorage accountStorage;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCashDeposit(CashDepositEvent event) {

        if (event.isCancelled()) return;

        val sender = event.getSender();
        val target = event.getTarget();
        val amount = event.getAmount();

        val targetAccount = accountStorage.findAccount(target);
        if (targetAccount == null) {

            event.setCancelled(true);
            sender.sendMessage(MessageValue.get(MessageValue::invalidTarget));
            return;

        }

        targetAccount.depositAmount(amount);

        sender.sendMessage(MessageValue.get(MessageValue::addAmount)
            .replace("$player", targetAccount.getOwner())
            .replace("$amount", NumberUtil.format(amount))
        );
    }

}
