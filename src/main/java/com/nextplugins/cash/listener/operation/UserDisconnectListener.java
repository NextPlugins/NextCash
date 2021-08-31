package com.nextplugins.cash.listener.operation;

import com.nextplugins.cash.storage.AccountStorage;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public final class UserDisconnectListener implements Listener {

    private final AccountStorage accountStorage;

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        accountStorage.getCache().synchronous().invalidate(player.getName());

    }

}
