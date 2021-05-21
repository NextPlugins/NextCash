package com.nextplugins.cash.manager;

import com.nextplugins.cash.configuration.GeneralConfiguration;
import com.nextplugins.cash.util.ItemBuilder;
import com.nextplugins.cash.util.text.ColorUtil;
import com.nextplugins.cash.util.text.NumberFormat;
import de.tr7zw.changeme.nbtapi.NBTItem;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class CheckManager {

    public static ItemStack createCheck(double checkValue) {
        val checkSection = GeneralConfiguration.get(GeneralConfiguration::checkItem);

        List<String> lore = new ArrayList<>();

        for (String line : checkSection.getStringList("lore")) {
            String colored = ColorUtil.colored(line);
            String $amount = colored.replace("$amount", NumberFormat.format(checkValue));
            lore.add($amount);
        }

        val checkItem = new ItemBuilder(Material.valueOf(checkSection.getString("material")))
                .name(ColorUtil.colored(checkSection.getString("display-name")))
                .setLore(lore)
                .changeItem(itemStack -> itemStack.setDurability((short) checkSection.getInt("data")))
                .wrap();

        val nbtItem = new NBTItem(checkItem);
        nbtItem.setDouble("value", checkValue);

        return nbtItem.getItem();
    }

}
