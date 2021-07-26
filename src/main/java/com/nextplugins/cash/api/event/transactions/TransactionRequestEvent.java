package com.nextplugins.cash.api.event.transactions;

import com.nextplugins.cash.api.event.CustomEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

@Getter
@Setter
public final class TransactionRequestEvent extends CustomEvent implements Cancellable {

    private final Player player;
    private final OfflinePlayer target;
    private final double amount;
    private boolean cancelled;

    public TransactionRequestEvent(Player player, OfflinePlayer target, double amount) {
        super(false);
        this.player = player;
        this.target = target;
        this.amount = amount;
    }
}
