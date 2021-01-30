package com.nextplugins.cash.command;

import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.api.model.account.Account;
import com.nextplugins.cash.configuration.MessageValue;
import com.nextplugins.cash.configuration.RankingConfiguration;
import com.nextplugins.cash.converter.PlayerPointConverter;
import com.nextplugins.cash.inventory.RankingInventory;
import com.nextplugins.cash.ranking.manager.LocationManager;
import com.nextplugins.cash.ranking.util.LocationUtil;
import com.nextplugins.cash.storage.AccountStorage;
import com.nextplugins.cash.storage.RankingStorage;
import com.nextplugins.cash.util.ColorUtil;
import com.nextplugins.cash.util.NumberFormat;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public final class CashCommand {

    private final NextCash plugin;
    private final AccountStorage accountStorage;
    private final RankingStorage rankingStorage;
    private final LocationManager locationManager;

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

    @Command(
            name = "cash.top",
            permission = "nextcash.command.top",
            async = true
    )
    public void cashResetCommand(Context<Player> context) {
        Player player = context.getSender();

        String rankingType = RankingConfiguration.get(RankingConfiguration::rankingType);

        LinkedHashMap<String, Double> rankingAccounts = rankingStorage.getRankingAccounts();

        if (rankingType.equalsIgnoreCase("CHAT")) {
            List<String> header = RankingConfiguration.get(RankingConfiguration::chatModelHeader);
            String body = RankingConfiguration.get(RankingConfiguration::chatModelBody);
            List<String> footer = RankingConfiguration.get(RankingConfiguration::chatModelFooter);

            header.forEach(player::sendMessage);

            AtomicInteger position = new AtomicInteger(1);

            rankingAccounts.forEach((owner, balance) -> player.sendMessage(body
                    .replace("$position", String.valueOf(position.getAndIncrement()))
                    .replace("$player", owner)
                    .replace("$amount", NumberFormat.format(balance))
            ));

            footer.forEach(player::sendMessage);
        } else if (rankingType.equalsIgnoreCase("INVENTORY")) {
            RankingInventory rankingInventory = new RankingInventory().init();
            rankingInventory.openInventory(player);
        } else {
            throw new IllegalArgumentException("Tipo de ranking inválido: + " + rankingType + ". (ranking.yml)");
        }

    }

    @Command(
            name = "cash.npc",
            permission = "nextcash.command.npc",
            target = CommandTarget.PLAYER,
            async = true
    )
    public void npcCommand(Context<Player> context) {
        Player player = context.getSender();

        player.sendMessage(ColorUtil.colored(
                "",
                "&a&lNextCash &8&l➡&f NPC Ranking",
                "",
                "&a/cash npc add (position) &8-&7 Adicione uma localização para spawn de um NPC da posição desejada.",
                "&a/cash npc remove (position) &8-&7 Remova a localização do NPC da posição desejada.",
                ""
        ));
    }

    @Command(
            name = "cash.npc.add",
            permission = "nextcash.command.npc",
            target = CommandTarget.PLAYER,
            async = true
    )
    public void npcAddCommand(Context<Player> context, int position) throws IOException {
        Player player = context.getSender();

        if (position <= 0) {
            player.sendMessage(MessageValue.get(MessageValue::wrongPosition));
        }

        if (locationManager.getLocationMap().containsKey(position)) {
            player.sendMessage(MessageValue.get(MessageValue::positionAlreadyDefined));
        }

        locationManager.getLocationMap().put(position, player.getLocation());

        List<String> locations = plugin.getNpcConfiguration().getStringList("npc.locations");
        locations.add(position + " " + LocationUtil.byLocationNoBlock(player.getLocation()));

        plugin.getNpcConfiguration().set("npc.locations", locations);
        plugin.getNpcConfiguration().save(plugin.getNpcFile());

        player.sendMessage(MessageValue.get(MessageValue::positionSuccessfulCreated));
    }

    @Command(
            name = "cash.npc.remove",
            permission = "nextcash.command.npc",
            target = CommandTarget.PLAYER,
            async = true
    )
    public void npcRemoveCommand(Context<Player> context, int position) throws IOException {
        Player player = context.getSender();

        if (position <= 0) {
            player.sendMessage(MessageValue.get(MessageValue::wrongPosition));
        }

        if (!locationManager.getLocationMap().containsKey(position)) {
            player.sendMessage(MessageValue.get(MessageValue::positionNotYetDefined));
        }

        List<String> locations = plugin.getNpcConfiguration().getStringList("npc.locations");
        locations.remove(position + " " + LocationUtil.byLocationNoBlock(locationManager.getLocation(position)));

        plugin.getNpcConfiguration().set("npc.locations", locations);
        plugin.getNpcConfiguration().save(plugin.getNpcFile());

        player.sendMessage(MessageValue.get(MessageValue::positionSuccessfulRemoved));
    }

    @Command(
            name = "cash.converter",
            aliases = { "convert" },
            permission = "cash.convert",
            target = CommandTarget.CONSOLE
    )
    public void handleConvertCommand(Context context) {
        PlayerPointConverter.builder()
                .plugin(plugin)
                .build()
                .start();
    }

}
