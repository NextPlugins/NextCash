package com.nextplugins.cash.listener;

import com.nextplugins.cash.storage.AccountStorage;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public final class UserConnectListener implements Listener {

    private final AccountStorage accountStorage;

    @EventHandler
    public void onConnect(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        accountStorage.getByName(player.getName());

    }

}
