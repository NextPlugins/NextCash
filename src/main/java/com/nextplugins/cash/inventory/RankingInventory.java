package com.nextplugins.cash.inventory;

import com.google.common.collect.Lists;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.paged.PagedInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.item.supplier.InventoryItemSupplier;
import com.henryfabio.minecraft.inventoryapi.viewer.configuration.border.Border;
import com.henryfabio.minecraft.inventoryapi.viewer.configuration.impl.ViewerConfigurationImpl;
import com.henryfabio.minecraft.inventoryapi.viewer.impl.paged.PagedViewer;
import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.configuration.RankingConfiguration;
import com.nextplugins.cash.storage.RankingStorage;
import com.nextplugins.cash.util.ItemBuilder;
import com.nextplugins.cash.util.NumberFormat;

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
    protected List<InventoryItemSupplier> createPageItems(PagedViewer viewer) {
        List<InventoryItemSupplier> items = Lists.newLinkedList();

        String headDisplayName = RankingConfiguration.get(RankingConfiguration::inventoryModelHeadDisplayName);
        List<String> headLore = RankingConfiguration.get(RankingConfiguration::inventoryModelHeadLore);

        AtomicInteger position = new AtomicInteger(1);

        rankingStorage.getRankingAccounts().forEach((owner, balance) -> {
            String replacedDisplayName = headDisplayName.replace("$player", owner)
                    .replace("$amount", NumberFormat.format(balance))
                    .replace("$position", String.valueOf(position.getAndIncrement()));

            List<String> replacedLore = Lists.newArrayList();

            for (String lore : headLore) {
                replacedLore.add(
                        lore.replace("$player", owner)
                                .replace("$amount", NumberFormat.format(balance))
                                .replace("$position", String.valueOf(position.getAndIncrement()))
                );
            }

            items.add(() -> InventoryItem.of(
                    new ItemBuilder(owner)
                            .name(replacedDisplayName)
                            .setLore(replacedLore)
                            .wrap()
            ));
        });

        return items;
    }
}
