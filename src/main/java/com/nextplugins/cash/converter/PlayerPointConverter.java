package com.nextplugins.cash.converter;

import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.connector.type.impl.MySQLDatabaseType;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.api.model.account.Account;
import com.nextplugins.libs.data.converter.Converter;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class PlayerPointConverter {

    // TODO: NEED A REMAKE

    private static final ScheduledExecutorService executors = Executors.newScheduledThreadPool(10);

    @Getter private final ConfigurationSection section;
    @Getter private final String tableName;

    private final SQLConnector connector;
    private final SQLExecutor internalExecutor;
    private final NextCash plugin;

    @Builder
    public PlayerPointConverter(NextCash plugin) {
        this.plugin = plugin;
        this.internalExecutor = plugin.getSqlExecutor();

        this.section = plugin.getConfig().getConfigurationSection("converter-playerpoints-mysql");

        this.tableName = getSection().getString("origin-table");
        this.connector = new MySQLDatabaseType(
                section.getString("address"),
                section.getString("username"),
                section.getString("password"),
                section.getString("database")).connect();
    }

    public void start() {
        final Logger logger = plugin.getLogger();
        AtomicInteger count = new AtomicInteger();

        Bukkit.getOnlinePlayers()
              .stream()
              .filter(player -> !player.isWhitelisted())
              .forEach(player -> {
                  player.kickPlayer(colorize("&c&lNEXT CASH&r\n&c&lA conversão começou, por favor, aguarde."));
              });
        Bukkit.getServer().setWhitelist(true);

        logger.info("Starting conversion...");
        connector.consumeConnection((connection) -> {
            Converter.<Account>builder()
                    .connection(connection)
                    .create()
                    .request("SELECT `points`, `playername` FROM  `" + getTableName() + "`")
                    .convert((resultSet) -> Account.builder()
                            .owner(Bukkit.getPlayer(String.valueOf(resultSet.get("playername"))))
                            .balance(resultSet.get("points"))
                            .build())
                    .responseEach((account) -> {
                        CompletableFuture.supplyAsync(() -> {
                            internalExecutor.updateQuery(String.format(
                                    "INSERT INTO `cash_accounts` VALUES ('%s', '%s')",
                                    account.getOwner().getName(),
                                    account.getBalance()
                            ));

                            return account;
                        }, executors).whenComplete((response, $) -> {
                            logger.info("Converting of " + account.getOwner().getName() + " was a success!");
                            count.incrementAndGet();
                        });
                    });
        });

        end(count.get());
    }

    public void end(int amount) {
        Bukkit.getServer().setWhitelist(false);

        plugin.getLogger().info("Conversion ended! " + amount + " players was converted.");
    }

    private String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
