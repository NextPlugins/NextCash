package com.nextplugins.cash.api.event.transactions;

import com.nextplugins.cash.api.event.CustomEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

@Data
@EqualsAndHashCode(callSuper = true)
public final class TransactionRequestEvent extends CustomEvent implements Cancellable {

    private final Player player;
    private final OfflinePlayer target;
    private final double amount;
    private boolean cancelled;

}
