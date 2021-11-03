package com.nextplugins.cash.inventory;

import com.google.common.collect.Lists;
import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.paged.PagedInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.item.supplier.InventoryItemSupplier;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.henryfabio.minecraft.inventoryapi.viewer.configuration.border.Border;
import com.henryfabio.minecraft.inventoryapi.viewer.configuration.impl.ViewerConfigurationImpl;
import com.henryfabio.minecraft.inventoryapi.viewer.impl.paged.PagedViewer;
import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.configuration.RankingConfiguration;
import com.nextplugins.cash.storage.RankingStorage;
import com.nextplugins.cash.util.ItemBuilder;
import com.nextplugins.cash.util.TimeUtils;
import lombok.val;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class RankingInventory extends PagedInventory {

    private final RankingStorage rankingStorage = NextCash.getInstance().getRankingStorage();

    public RankingInventory() {
        super(
            "nextcash.ranking.inventory",
            RankingConfiguration.get(RankingConfiguration::inventoryModelTitle),
            4 * 9
        );
    }

    @Override
    protected void configureViewer(PagedViewer viewer) {
        ViewerConfigurationImpl.Paged configuration = viewer.getConfiguration();

        configuration.itemPageLimit(21);
        configuration.border(Border.of(1, 1, 2, 1));
    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {
        editor.setItem(0, restTimeUpdate());
    }

    @Override
    protected List<InventoryItemSupplier> createPageItems(PagedViewer viewer) {
        List<InventoryItemSupplier> items = Lists.newLinkedList();

        val headDisplayName = RankingConfiguration.get(RankingConfiguration::inventoryModelHeadDisplayName);
        val headLore = RankingConfiguration.get(RankingConfiguration::inventoryModelHeadLore);

        val position = new AtomicInteger(1);

        rankingStorage.getRankingAccounts().forEach((owner, balance) -> {

            val value = position.getAndIncrement();
            items.add(() -> {
                val group = rankingStorage.getGroupManager().getGroup(owner);

                val replacedDisplayName = headDisplayName
                        .replace("$player", owner)
                        .replace("$amount", balance)
                        .replace("$prefix", group.getPrefix())
                        .replace("$suffix", group.getSuffix())
                        .replace("$position", String.valueOf(value));

                List<String> replacedLore = Lists.newArrayList();
                for (val lore : headLore) {
                    replacedLore.add(lore
                            .replace("$player", owner)
                            .replace("$amount", balance)
                            .replace("$position", String.valueOf(value))
                    );
                }

                return InventoryItem.of(
                        new ItemBuilder(owner)
                                .name(replacedDisplayName)
                                .setLore(replacedLore)
                                .wrap()
                );
            });
        });

        return items;
    }

    private InventoryItem restTimeUpdate() {
        return InventoryItem.of(new ItemBuilder("MHF_QUESTION")
            .name("&6Próxima atualização")
            .setLore(
                "&7A próxima atualização do ranking será em",
                "&e" + TimeUtils.format(rankingStorage.getNextUpdate() - System.currentTimeMillis())
            )
            .wrap()
        );
    }
}
