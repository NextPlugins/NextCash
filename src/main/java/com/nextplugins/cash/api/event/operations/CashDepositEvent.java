package com.nextplugins.cash.api.event.operations;

import com.nextplugins.cash.api.event.CustomEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

@Data
@EqualsAndHashCode(callSuper = true)
public final class CashDepositEvent extends CustomEvent implements Cancellable {

    private final CommandSender sender;
    private final OfflinePlayer target;
    private final double amount;
    private boolean cancelled;

}
