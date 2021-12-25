package com.nextplugins.cash.listener.check;

import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.util.text.ColorUtil;
import lombok.val;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateCheckerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPermission("nextcash.admin")) return;

        val updateChecker = NextCash.getInstance().getUpdateChecker();
        if (!updateChecker.canUpdate()) return;

        val newVersionComponent = new TextComponent(String.format(
                " Uma nova versão do NextCash está disponível (%s » %s)",
                updateChecker.getCurrentVersion(),
                updateChecker.getMoreRecentVersion()
        ));

        val downloadComponent = new TextComponent(" Clique aqui para ir até o local de download.");
        val channelComponent = new TextComponent(TextComponent.fromLegacyText(ColorUtil.colored(
                " &7Canal de atualização: " + getChannel(updateChecker.getMoreRecentVersion()))
        ));

        val hoverText = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ColorUtil.colored("&7Este link irá levar até o github do plugin")));
        val clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, updateChecker.getDownloadLink());

        newVersionComponent.setColor(ChatColor.GREEN);
        downloadComponent.setColor(ChatColor.GRAY);

        newVersionComponent.setHoverEvent(hoverText);
        downloadComponent.setHoverEvent(hoverText);
        channelComponent.setHoverEvent(hoverText);

        newVersionComponent.setClickEvent(clickEvent);
        downloadComponent.setClickEvent(clickEvent);
        channelComponent.setClickEvent(clickEvent);

        val player = event.getPlayer();
        val spigotPlayer = player.spigot();

        // avoid chat cleaners when join
        Bukkit.getScheduler().runTaskLater(NextCash.getInstance(), () -> {
            player.sendMessage("");
            spigotPlayer.sendMessage(newVersionComponent);
            spigotPlayer.sendMessage(downloadComponent);
            spigotPlayer.sendMessage(channelComponent);
            player.sendMessage("");
        }, 5 * 20L);
    }

    private String getChannel(String moreRecentVersion) {
        return moreRecentVersion.contains("-") ? "&6Beta" : "&aEstável";
    }

}