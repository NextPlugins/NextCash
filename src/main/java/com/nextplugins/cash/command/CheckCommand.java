package com.nextplugins.cash.command;

import com.nextplugins.cash.configuration.GeneralConfiguration;
import com.nextplugins.cash.configuration.MessageValue;
import com.nextplugins.cash.manager.CheckManager;
import com.nextplugins.cash.storage.AccountStorage;
import com.nextplugins.cash.util.text.NumberFormat;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@RequiredArgsConstructor
public final class CheckCommand {

    private final AccountStorage accountStorage;

    @Command(
            name = "check",
            aliases = {"cheque"},
            description = "Sistema de cheque.",
            permission = "nextcash.command.check",
            async = true
    )
    public void checkCommand(Context<CommandSender> context) {
        val helpMessage = MessageValue.get(MessageValue::checkHelpCommand);

        for (String message : helpMessage) {
            context.sendMessage(message);
        }
    }

    @Command(
            name = "check.create",
            aliases = {"criar"},
            description = "Crie um cheque com um certo valor.",
            permission = "nextcash.command.check.create",
            usage = "/cheque criar (valor) [jogador]",
            target = CommandTarget.PLAYER,
            async = true
    )
    public void createCheckCommand(Context<Player> context, double amount, @Optional Player target) {
        val player = context.getSender();

        val minValue = GeneralConfiguration.get(GeneralConfiguration::checkMinimumValue);
        if (amount < minValue) {
            player.sendMessage(
                    MessageValue.get(MessageValue::checkMinimumValue)
                            .replace("$amount", NumberFormat.format(minValue))
            );
            return;
        }

        val account = accountStorage.getByName(player.getName());
        if (!account.hasAmount(amount)) {
            player.sendMessage(MessageValue.get(MessageValue::checkInsufficientValue));
            return;
        }

        account.withdrawAmount(amount);

        player.sendMessage(
                MessageValue.get(MessageValue::checkCreated)
                        .replace("$checkValue", NumberFormat.format(amount))
        );

        val checkItem = CheckManager.createCheck(amount);
        if (target != null) {

            target.sendMessage(
                    MessageValue.get(MessageValue::checkReceived)
                            .replace("$checkValue", NumberFormat.format(amount))
                            .replace("$sender", player.getName())
            );

            dropItem(target, target.getInventory().addItem(checkItem));
            return;

        }

        dropItem(player, player.getInventory().addItem(checkItem));
    }

    private void dropItem(Player player, Map<Integer, ItemStack> addItem) {
        for (ItemStack itemStack : addItem.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
        }
    }

}
