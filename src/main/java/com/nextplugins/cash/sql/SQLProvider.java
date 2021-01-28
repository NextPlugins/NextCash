package com.nextplugins.cash.sql;

import com.google.inject.Inject;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.connector.type.SQLDatabaseType;
import com.henryfabio.sqlprovider.connector.type.impl.MySQLDatabaseType;
import com.henryfabio.sqlprovider.connector.type.impl.SQLiteDatabaseType;
import com.nextplugins.cash.NextCash;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.logging.Logger;

public final class SQLProvider {

    @Inject private static NextCash javaPlugin;

    public static SQLConnector setup() {
        FileConfiguration configuration = javaPlugin.getConfig();
        ConfigurationSection databaseConfiguration = configuration.getConfigurationSection("database");

        String sqlType = databaseConfiguration.getString("type");

        Logger logger = javaPlugin.getLogger();

        SQLConnector sqlConnector;

        if (sqlType.equalsIgnoreCase("mysql")) {

            ConfigurationSection mysqlSection = databaseConfiguration.getConfigurationSection("mysql");
            sqlConnector = mysqlDatabaseType(mysqlSection).connect();
            logger.info("Conexão com o banco de dados (MySQL) realizada com sucesso.");

        } else if (sqlType.equalsIgnoreCase("sqlite")) {

            ConfigurationSection sqliteSection = databaseConfiguration.getConfigurationSection("sqlite");
            sqlConnector = sqliteDatabaseType(sqliteSection).connect();
            logger.info("Conexão com o banco de dados (SQLite) realizada com sucesso.");

        } else {

            logger.severe("O tipo de database selecionado não é válido.");
            return null;

        }

        return sqlConnector;

    }

    private static SQLDatabaseType sqliteDatabaseType(ConfigurationSection section) {
        return SQLiteDatabaseType.builder()
                .file(new File(javaPlugin.getDataFolder(), section.getString("file")))
                .build();
    }

    private static SQLDatabaseType mysqlDatabaseType(ConfigurationSection section) {
        return MySQLDatabaseType.builder()
                .address(section.getString("address"))
                .username(section.getString("username"))
                .password(section.getString("password"))
                .database(section.getString("database"))
                .build();
    }

}
