package com.nextplugins.cash.api.model.account;

import lombok.Builder;
import lombok.Data;
import org.bukkit.OfflinePlayer;

@Data
@Builder
public class Account {

    private OfflinePlayer owner;
    private double balance;

}
