package com.nextplugins.cash.util.text;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

public final class TextLogger {

    private final ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();
    private static final String PLUGIN_NAME = "[NextCash]";

    public void debug(String message) {
        consoleSender.sendMessage(ChatColor.GREEN + PLUGIN_NAME + " (DEBUG) " + ChatColor.WHITE + message);
    }

    public void info(String message) {
        consoleSender.sendMessage(ChatColor.AQUA + PLUGIN_NAME + " (INFO) " + ChatColor.WHITE + message);
    }

    public void warn(String message) {
        consoleSender.sendMessage(ChatColor.YELLOW + PLUGIN_NAME + " (WARN) " + ChatColor.WHITE + message);
    }

    public void error(String message) {
        consoleSender.sendMessage(ChatColor.RED + PLUGIN_NAME + " (ERROR) " + ChatColor.WHITE + message);
    }

}
