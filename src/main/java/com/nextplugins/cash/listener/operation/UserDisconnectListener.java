package com.nextplugins.cash.listener.operation;

import com.nextplugins.cash.storage.AccountStorage;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public final class UserDisconnectListener implements Listener {

    private final AccountStorage accountStorage;

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        accountStorage.saveOne(event.getPlayer().getName());
        accountStorage.getCache().remove(event.getPlayer().getName());
    }

}
