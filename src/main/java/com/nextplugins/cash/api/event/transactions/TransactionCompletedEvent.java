package com.nextplugins.cash.api.event.transactions;

import com.nextplugins.cash.api.event.CustomEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@Getter
@Setter
public final class TransactionCompletedEvent extends CustomEvent {

    private final Player player;
    private final OfflinePlayer target;
    private final double amount;

    public TransactionCompletedEvent(Player player, OfflinePlayer target, double amount) {
        super(false);
        this.player = player;
        this.target = target;
        this.amount = amount;
    }
}
