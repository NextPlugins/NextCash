package com.nextplugins.cash.util.text;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

public final class TextLogger {

    private final ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();
    private final String pluginName = "[NextCash]";

    public void blankLine() {
        consoleSender.sendMessage("§r");
    }

    public void debug(String message) {
        consoleSender.sendMessage(ChatColor.GREEN + pluginName + " (DEBUG) " + ChatColor.WHITE + message);
    }

    public void info(String message) {
        consoleSender.sendMessage(ChatColor.AQUA + pluginName + " (INFO) " + ChatColor.WHITE + message);
    }

    public void warn(String message) {
        consoleSender.sendMessage(ChatColor.YELLOW + pluginName + " (WARN) " + ChatColor.WHITE + message);
    }

    public void error(String message) {
        consoleSender.sendMessage(ChatColor.RED + pluginName + " (ERROR) " + ChatColor.WHITE + message);
    }

}
