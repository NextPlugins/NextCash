package com.nextplugins.cash.command;

import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.api.event.operations.CashDepositEvent;
import com.nextplugins.cash.api.event.operations.CashSetEvent;
import com.nextplugins.cash.api.event.operations.CashWithdrawEvent;
import com.nextplugins.cash.api.event.transactions.TransactionCompletedEvent;
import com.nextplugins.cash.api.model.account.Account;
import com.nextplugins.cash.configuration.MessageValue;
import com.nextplugins.cash.configuration.RankingConfiguration;
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
import org.bukkit.Bukkit;
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
            usage = "/cash <jogador>",
            description = "Utilize para ver a sua quantia de Cash, ou a de outro jogador.",
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
            name = "cash.pay",
            aliases = {"enviar"},
            usage = "/cash enviar {jogador} {quantia}",
            description = "Utilize para enviar uma quantia da sua conta para outra.",
            permission = "nextcash.command.pay",
            async = true
    )
    public void cashPayCommand(Context<Player> context, Player target, double amount) {
        Player player = context.getSender();

        if (target != null) {
            if (target.equals(player)) {
                player.sendMessage(MessageValue.get(MessageValue::isYourself));
                return;
            }

            Account account = accountStorage.getByName(player.getName());
            Account targetAccount = accountStorage.getByName(target.getName());

            if (account.hasAmount(amount)) {
                targetAccount.depositAmount(amount);
                account.withdrawAmount(amount);

                player.sendMessage(
                        MessageValue.get(MessageValue::paid).replace("$player", target.getName())
                                .replace("$amount", NumberFormat.format(amount))
                );

                target.sendMessage(
                        MessageValue.get(MessageValue::received).replace("$player", player.getName())
                                .replace("$amount", NumberFormat.format(amount))
                );

                Bukkit.getPluginManager().callEvent(new TransactionCompletedEvent(player, target, amount));
            } else {
                player.sendMessage(MessageValue.get(MessageValue::insufficientAmount));
            }
        } else {
            player.sendMessage(MessageValue.get(MessageValue::invalidTarget));
        }

    }

    @Command(
            name = "cash.help",
            aliases = {"ajuda", "comandos"},
            description = "Utilize para receber ajuda com os comandos do plugin.",
            permission = "nextcash.command.help",
            async = true
    )
    public void cashHelpCommand(Context<Player> context) {
        Player player = context.getSender();

        if (player.hasPermission("nextcash.command.help.staff")) {
            ColorUtil.colored(MessageValue.get(MessageValue::helpCommandStaff)).forEach(player::sendMessage);
        } else {
            ColorUtil.colored(MessageValue.get(MessageValue::helpCommand)).forEach(player::sendMessage);
        }
    }

    @Command(
            name = "cash.set",
            aliases = {"alterar"},
            usage = "/cash set {jogador} {quantia}",
            description = "Utilize para alterar a quantia de cash de alguém.",
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

            Bukkit.getPluginManager().callEvent(new CashSetEvent(player, target, amount));
        } else {
            player.sendMessage(MessageValue.get(MessageValue::invalidTarget));
        }

    }

    @Command(
            name = "cash.add",
            aliases = {"adicionar", "deposit", "depositar"},
            usage = "/cash adicionar {jogador} {quantia} ",
            description = "Utilize para adicionar uma quantia de cash para alguém.",
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

            Bukkit.getPluginManager().callEvent(new CashDepositEvent(player, target, amount));
        } else {
            player.sendMessage(MessageValue.get(MessageValue::invalidTarget));
        }

    }

    @Command(
            name = "cash.remove",
            aliases = {"remover", "withdraw", "retirar"},
            usage = "/cash remover {jogador} {quantia}",
            description = "Utilize para remover uma quantia de cash de alguém.",
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

            Bukkit.getPluginManager().callEvent(new CashWithdrawEvent(player, target, amount));
        } else {
            player.sendMessage(MessageValue.get(MessageValue::invalidTarget));
        }

    }

    @Command(
            name = "cash.reset",
            aliases = {"zerar"},
            usage = "/cash zerar {jogador}",
            description = "Utilize para zerar a quantia de cash de alguém.",
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
            aliases = {"ranking", "podio"},
            description = "Utilize para ver os jogadores com mais cash do servidor.",
            permission = "nextcash.command.top",
            async = true
    )
    public void cashTopCommand(Context<Player> context) {
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
            usage = "/cash npc",
            description = "Utilize para ver a ajuda para os comandos do sistema de NPC.",
            permission = "nextcash.command.npc.help",
            target = CommandTarget.PLAYER,
            async = true
    )
    public void npcCommand(Context<Player> context) {
        Player player = context.getSender();

        ColorUtil.colored(MessageValue.get(MessageValue::npcHelp)).forEach(player::sendMessage);
    }

    @Command(
            name = "cash.npc.add",
            aliases = {"npc.adicionar"},
            usage = "/cash npc add {posição}",
            description = "Utilize para definir uma localização de spawn de NPC de certa posição.",
            permission = "nextcash.command.npc.add",
            target = CommandTarget.PLAYER,
            async = true
    )
    public void npcAddCommand(Context<Player> context, int position) throws IOException {
        Player player = context.getSender();

        if (position <= 0) {
            player.sendMessage(MessageValue.get(MessageValue::wrongPosition));
        }

        int limit = RankingConfiguration.get(RankingConfiguration::rankingLimit);

        if (position > limit) {
            player.sendMessage(MessageValue.get(MessageValue::positionReachedLimit)
                    .replace("$limit", String.valueOf(limit))
            );
        }

        if (locationManager.getLocationMap().containsKey(position)) {
            player.sendMessage(MessageValue.get(MessageValue::positionAlreadyDefined));
        }

        locationManager.getLocationMap().put(position, player.getLocation());

        List<String> locations = plugin.getNpcConfiguration().getStringList("npc.locations");
        locations.add(position + " " + LocationUtil.byLocationNoBlock(player.getLocation()));

        plugin.getNpcConfiguration().set("npc.locations", locations);
        plugin.getNpcConfiguration().save(plugin.getNpcFile());

        player.sendMessage(MessageValue.get(MessageValue::positionSuccessfulCreated).replace("$position", String.valueOf(position)));
    }

    @Command(
            name = "cash.npc.remove",
            aliases = {"npc.remover"},
            usage = "/cash npc remove {posição}",
            description = "Utilize para remover uma localização de spawn de NPC de certa posição.",
            permission = "nextcash.command.npc.remove",
            target = CommandTarget.PLAYER,
            async = true
    )
    public void npcRemoveCommand(Context<Player> context, int position) throws IOException {
        Player player = context.getSender();

        if (position <= 0) {
            player.sendMessage(MessageValue.get(MessageValue::wrongPosition));
        }

        int limit = RankingConfiguration.get(RankingConfiguration::rankingLimit);

        if (position > limit) {
            player.sendMessage(MessageValue.get(MessageValue::positionReachedLimit)
                    .replace("$limit", String.valueOf(limit))
            );
        }

        if (!locationManager.getLocationMap().containsKey(position)) {
            player.sendMessage(MessageValue.get(MessageValue::positionNotYetDefined));
        }

        List<String> locations = plugin.getNpcConfiguration().getStringList("npc.locations");
        locations.remove(position + " " + LocationUtil.byLocationNoBlock(locationManager.getLocation(position)));

        plugin.getNpcConfiguration().set("npc.locations", locations);
        plugin.getNpcConfiguration().save(plugin.getNpcFile());

        player.sendMessage(MessageValue.get(MessageValue::positionSuccessfulRemoved).replace("$position", String.valueOf(position)));
    }

}
