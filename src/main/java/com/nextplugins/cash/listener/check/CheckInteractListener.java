package com.nextplugins.cash.listener.check;

import com.nextplugins.cash.configuration.MessageValue;
import com.nextplugins.cash.storage.AccountStorage;
import com.nextplugins.cash.util.text.NumberFormat;
import de.tr7zw.changeme.nbtapi.NBTItem;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.var;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

@RequiredArgsConstructor
public final class CheckInteractListener implements Listener {

    private final AccountStorage accountStorage;

    @EventHandler(ignoreCancelled = true)
    public void onCheckActivate(PlayerInteractEvent event) {
        val item = event.getItem();
        if (item == null || item.getType() == Material.AIR) return;

        val player = event.getPlayer();

        val checkField = "NextCash_VALUE";

        val nbtItem = new NBTItem(item);
        if (!nbtItem.hasKey(checkField)) return;

        player.setItemInHand(null);

        var value = nbtItem.getDouble(checkField) * item.getAmount();

        if (player.isSneaking()) {
            val contents = player.getInventory().getContents();

            for (int i = 0; i < contents.length; i++) {
                val content = contents[i];
                if (content == null || content.getType() == Material.AIR) continue;

                val contentNBT = new NBTItem(content);
                if (!contentNBT.hasKey(checkField)) continue;

                value += contentNBT.getDouble(checkField) * content.getAmount();
                contents[i] = null;
            }

            player.getInventory().setContents(contents);
        }

        val account = accountStorage.findAccount(player);

        account.depositAmount(value);

        player.sendMessage(
                MessageValue.get(MessageValue::checkUsed)
                        .replace("$checkAmount", NumberFormat.format(item.getAmount()))
                        .replace("$checkTotalValue", NumberFormat.format(value))
        );
    }

}
