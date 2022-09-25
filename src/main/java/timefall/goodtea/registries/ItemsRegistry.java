package timefall.goodtea.registries;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import timefall.goodtea.GoodTea;

public class ItemsRegistry {

    // Items
    public static final Item TEA_CUP = registerItem("tea_cup",
            new Item(new FabricItemSettings().group(GoodTea.GOOD_TEA)));

    // Herbs and Spices
    public static final Item CLOVES = registerItem("cloves",
            new Item(new FabricItemSettings().group(GoodTea.GOOD_TEA)));
    public static final Item GINGER_ROOT = registerItem("ginger_root",
            new Item(new FabricItemSettings().group(GoodTea.GOOD_TEA)));
    public static final Item GRAPE_LEAF = registerItem("grape_leaf",
            new Item(new FabricItemSettings().group(GoodTea.GOOD_TEA)));
    public static final Item SAGE_LEAF = registerItem("sage_leaf",
            new Item(new FabricItemSettings().group(GoodTea.GOOD_TEA)));
    public static final Item SPEARMINT_LEAF = registerItem("spearmint_leaf",
            new Item(new FabricItemSettings().group(GoodTea.GOOD_TEA)));
    public static final Item TURMERIC_ROOT = registerItem("turmeric_root",
            new Item(new FabricItemSettings().group(GoodTea.GOOD_TEA)));

    // Teas
    public static final Item SPEARMINT_TEA = registerItem("spearmint_tea",
            new Item(new FabricItemSettings().group(GoodTea.GOOD_TEA)));

    // Seeds
    public static final Item SPEARMINT_SEEDS = registerItem("spearmint_seeds",
            new AliasedBlockItem(BlocksRegistry.SPEARMINT_BLOCK, new FabricItemSettings().group(GoodTea.GOOD_TEA)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(GoodTea.MOD_ID, name), item);
    }

    public static void registerItems() {
        GoodTea.LOGGER.info("Steeping items to make some " + GoodTea.MOD_ID + "!");
    }
}
