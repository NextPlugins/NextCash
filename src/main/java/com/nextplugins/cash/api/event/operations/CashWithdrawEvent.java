package com.nextplugins.cash.api.event.operations;

import com.nextplugins.cash.api.event.CustomEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;

@Data
@EqualsAndHashCode(callSuper = true)
public final class CashWithdrawEvent extends CustomEvent {

    private final Player player;
    private final Player target;
    private final double amount;

}
