package com.nextplugins.cash.api.model.account;

import lombok.Builder;
import lombok.Data;
import org.bukkit.OfflinePlayer;

@Data
@Builder
public class Account {

    private String owner;

    private double balance;

    private boolean receiveCash;

    public synchronized void depositAmount(double amount) {
        this.balance = balance + amount;
    }

    public synchronized void withdrawAmount(double amount) {
        this.balance = balance - amount;
    }

    public synchronized boolean hasAmount(double amount) {
        return this.balance >= amount;
    }

}
