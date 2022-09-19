package timefall.goodtea.registries;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import timefall.goodtea.GoodTea;

public class ItemsRegistry {

    public static final Item SPEARMINT_LEAF = registerItem("spearmint_leaf",
            new Item(new FabricItemSettings().group(GoodTea.GOOD_TEA)));
    public static final Item SPEARMINT_SEEDS = registerItem("spearmint_seeds",
            new AliasedBlockItem(BlocksRegistry.SPEARMINT_BLOCK, new FabricItemSettings().group(GoodTea.GOOD_TEA)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(GoodTea.MOD_ID, name), item);
    }

    public static void registerItems() {
        GoodTea.LOGGER.info("Steeping items to make some " + GoodTea.MOD_ID + "!");
    }
}
