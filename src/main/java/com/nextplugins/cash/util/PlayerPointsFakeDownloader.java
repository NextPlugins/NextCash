package com.nextplugins.cash.util;

import com.nextplugins.cash.NextCash;
import lombok.Data;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;

@Data(staticConstructor = "of")
public final class PlayerPointsFakeDownloader {

    private final NextCash plugin;

    public void download() {
        final String parent = plugin.getDataFolder().getParent();

        final File file = new File(parent, "PlayerPoints-FAKE.jar");

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
