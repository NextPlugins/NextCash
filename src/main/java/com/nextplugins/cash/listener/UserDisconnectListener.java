package com.nextplugins.cash.listener;

import com.google.inject.Inject;
import com.nextplugins.cash.storage.AccountStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class UserDisconnectListener implements Listener {

    @Inject private AccountStorage accountStorage;

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        accountStorage.purge(player.getName());

    }

}
