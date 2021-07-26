package com.nextplugins.cash.api.event.operations;

import com.nextplugins.cash.api.event.CustomEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;

@Getter
@Setter
public final class CashDepositEvent extends CustomEvent implements Cancellable {

    private final CommandSender sender;
    private final OfflinePlayer target;
    private final double amount;
    private boolean cancelled;

    public CashDepositEvent(CommandSender sender, OfflinePlayer target, double amount) {
        super(false);
        this.sender = sender;
        this.target = target;
        this.amount = amount;
    }
}
