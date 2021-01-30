package com.nextplugins.cash.converter.impl;

import com.henryfabio.sqlprovider.connector.type.impl.SQLiteDatabaseType;
import com.nextplugins.cash.converter.utils.SQLiteConverter;
import lombok.Builder;

public class PlayerPointConverter extends SQLiteConverter {

    @Builder
    public PlayerPointConverter(SQLiteDatabaseType databaseType) {
        super("playername", "amount", "playerpoints", databaseType);
    }

}
