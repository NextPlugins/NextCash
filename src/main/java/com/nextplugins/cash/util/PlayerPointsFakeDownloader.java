package com.nextplugins.cash.util;

import com.nextplugins.cash.NextCash;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;

@RequiredArgsConstructor(staticName = "of")
public final class PlayerPointsFakeDownloader {

    private final NextCash plugin;

    public void download() {
        if (!plugin.getConfig().getBoolean("plugin.playerPoints")) return;

        val parent = plugin.getDataFolder().getParent();
        val file = new File(parent, "PlayerPoints-FAKE.jar");

        if (!file.exists()) {
            try {
                FileUtils.copyURLToFile(
                    new URL("https://github.com/NextPlugins/PlayerPoints-FAKE/releases/download/1.0.0/playerpoints-fake-1.0.0.jar"),
                    file
                );
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

}
