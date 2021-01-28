package com.nextplugins.cash.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.dao.AccountDAO;
import com.nextplugins.cash.storage.AccountStorage;
import com.nextplugins.cash.storage.RankingStorage;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data(staticConstructor = "of")
public class PluginInjector extends AbstractModule {

    private final NextCash nextCash;

    @Override
    protected void configure() {
        bind(NextCash.class).toInstance(nextCash);
        bind(SQLExecutor.class).toInstance(nextCash.getSqlExecutor());

        bind(AccountDAO.class).toInstance(nextCash.getAccountDAO());
        bind(AccountStorage.class).toInstance(nextCash.getAccountStorage());
        bind(RankingStorage.class).toInstance(nextCash.getRankingStorage());

        requestStaticInjection(NextCash.class);
    }

    public Injector get() {
        return Guice.createInjector(this);
    }

}
