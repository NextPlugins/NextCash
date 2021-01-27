package com.nextplugins.cash.command;

import com.nextplugins.cash.api.model.account.Account;
import com.nextplugins.cash.configuration.MessageValue;
import com.nextplugins.cash.storage.AccountStorage;
import com.nextplugins.cash.util.NumberFormat;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public final class CashCommand {

    private final AccountStorage accountStorage;

    @Command(
            name = "cash",
            description = "Utilize para ver a sua quantia de Cash.",
            permission = "nextcash.command.see",
            target = CommandTarget.PLAYER,
            async = true
    )
    public void cashCommand(Context<Player> context, @Optional Player target) {
        Player player = context.getSender();

        if (target == null) {
            double balance = accountStorage.getByName(player.getName()).getBalance();

            player.sendMessage(MessageValue.get(MessageValue::seeBalance)
                    .replace("$amount", NumberFormat.format(balance))
            );
        } else {
            double targetBalance = accountStorage.getByName(target.getName()).getBalance();

            player.sendMessage(MessageValue.get(MessageValue::seeOtherBalance)
                    .replace("$player", target.getName())
                    .replace("$amount", NumberFormat.format(targetBalance))
            );
        }

    }

    @Command(
            name = "cash.set",
            permission = "nextcash.command.set",
            target = CommandTarget.PLAYER,
            async = true
    )
    public void cashSetCommand(Context<Player> context, Player target, double amount) {
        Player player = context.getSender();

        if (target != null) {
            Account targetAccount = accountStorage.getByName(target.getName());

            targetAccount.setBalance(amount);

            player.sendMessage(MessageValue.get(MessageValue::setAmount)
                    .replace("$player", targetAccount.getOwner().getName())
                    .replace("$amount", NumberFormat.format(targetAccount.getBalance()))
            );
        } else {
            player.sendMessage(MessageValue.get(MessageValue::invalidTarget));
        }

    }

    @Command(
            name = "cash.add",
            permission = "nextcash.command.add",
            target = CommandTarget.PLAYER,
            async = true
    )
    public void cashAddCommand(Context<Player> context, Player target, double amount) {
        Player player = context.getSender();

        if (target != null) {
            Account targetAccount = accountStorage.getByName(target.getName());

            targetAccount.depositAmount(amount);

            player.sendMessage(MessageValue.get(MessageValue::addAmount)
                    .replace("$player", targetAccount.getOwner().getName())
                    .replace("$amount", NumberFormat.format(targetAccount.getBalance()))
            );
        } else {
            player.sendMessage(MessageValue.get(MessageValue::invalidTarget));
        }

    }

    @Command(
            name = "cash.remove",
            permission = "nextcash.command.add",
            target = CommandTarget.PLAYER,
            async = true
    )
    public void cashRemoveCommand(Context<Player> context, Player target, double amount) {
        Player player = context.getSender();

        if (target != null) {
            Account targetAccount = accountStorage.getByName(target.getName());

            targetAccount.withdrawAmount(amount);

            player.sendMessage(MessageValue.get(MessageValue::removeAmount)
                    .replace("$player", targetAccount.getOwner().getName())
                    .replace("$amount", NumberFormat.format(targetAccount.getBalance()))
            );
        } else {
            player.sendMessage(MessageValue.get(MessageValue::invalidTarget));
        }

    }

    @Command(
            name = "cash.reset",
            permission = "nextcash.command.reset",
            target = CommandTarget.PLAYER,
            async = true
    )
    public void cashResetCommand(Context<Player> context, Player target) {
        Player player = context.getSender();

        if (target != null) {
            Account targetAccount = accountStorage.getByName(target.getName());

            targetAccount.setBalance(0);

            player.sendMessage(MessageValue.get(MessageValue::resetBalance)
                    .replace("$player", targetAccount.getOwner().getName())
            );
        } else {
            player.sendMessage(MessageValue.get(MessageValue::invalidTarget));
        }

    }

}