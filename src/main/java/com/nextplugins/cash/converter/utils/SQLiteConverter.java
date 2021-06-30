package com.nextplugins.cash.converter.utils;

import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.connector.type.impl.SQLiteDatabaseType;
import com.nextplugins.cash.api.model.account.Account;
import com.nextplugins.cash.converter.PluginConverter;
import com.nextplugins.libs.data.converter.Converter;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@AllArgsConstructor
public class SQLiteConverter implements PluginConverter {

    private final String name;
    private final String balance;
    private final String table;
    private final SQLiteDatabaseType databaseType;

    @Override
    public ExecutorService executors() {
        // avoid lock file

        return Executors.newFixedThreadPool(3);
    }

    @Override
    public SQLConnector originConnector() {
        return databaseType.connect();
    }

    @Override
    public String nameField() {
        return name;
    }

    @Override
    public String balanceField() {
        return balance;
    }

    @Override
    public String tableName() {
        return table;
    }

    @Override
    public void convert() {
        final String sql = "SELECT `" + nameField() + "`, `" + balanceField() + "` FROM `" + tableName() + "`;";
        final SQLConnector connector = originConnector();
        final ExecutorService executors = executors();

        connector.consumeConnection(connection -> Converter.<Account>builder()
                .connection(connection)
                .create()
                .request(sql)
                .convert(resultSet -> Account.builder()
                        .owner(String.valueOf(resultSet.get(nameField())))
                        .balance(resultSet.get(balanceField()))
                        .build())
                .responseEach(value -> save(value, executors)));
    }

}
