package com.nextplugins.cash.sql;

import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.connector.type.SQLDatabaseType;
import com.henryfabio.sqlprovider.connector.type.impl.MySQLDatabaseType;
import com.henryfabio.sqlprovider.connector.type.impl.SQLiteDatabaseType;
import com.nextplugins.cash.NextCash;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.Objects;

@RequiredArgsConstructor(staticName = "of")
public final class SQLProvider {

    private final NextCash plugin;

    public SQLConnector setup() {

        FileConfiguration configuration = plugin.getConfig();
        ConfigurationSection databaseConfiguration = configuration.getConfigurationSection("database");

        String sqlType = Objects.requireNonNull(databaseConfiguration).getString("type");

        val logger = plugin.getTextLogger();

        SQLConnector sqlConnector;

        if (Objects.requireNonNull(sqlType).equalsIgnoreCase("mysql")) {
            ConfigurationSection mysqlSection = databaseConfiguration.getConfigurationSection("mysql");

            sqlConnector = mysqlDatabaseType(Objects.requireNonNull(mysqlSection)).connect();

            logger.info("Conexão com o banco de dados (MySQL) realizada com sucesso.");
        } else if (sqlType.equalsIgnoreCase("sqlite")) {
            ConfigurationSection sqliteSection = databaseConfiguration.getConfigurationSection("sqlite");

            sqlConnector = sqliteDatabaseType(Objects.requireNonNull(sqliteSection)).connect();

            logger.info("Conexão com o banco de dados (SQLite) realizada com sucesso.");
            logger.warn("Recomendamos o uso do banco de dados MySQL.");
        } else {
            logger.error("O tipo de database selecionado não é válido.");

            return null;
        }

        return sqlConnector;

    }

    private SQLDatabaseType sqliteDatabaseType(ConfigurationSection section) {
        return SQLiteDatabaseType.builder()
            .file(new File(plugin.getDataFolder(), Objects.requireNonNull(section.getString("file"))))
            .build();
    }

    private SQLDatabaseType mysqlDatabaseType(ConfigurationSection section) {
        return MySQLDatabaseType.builder()
            .address(section.getString("address"))
            .username(section.getString("username"))
            .password(section.getString("password"))
            .database(section.getString("database"))
            .build();
    }

}
