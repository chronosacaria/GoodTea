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

    // Tea Leaves
    public static final Item TEA_LEAF = registerItem("tea_leaf",
            new Item(new FabricItemSettings().group(GoodTea.GOOD_TEA)));
    public static final Item BLACK_TEA_LEAF = registerItem("black_tea_leaf",
            new Item(new FabricItemSettings().group(GoodTea.GOOD_TEA)));
    public static final Item OOLONG_TEA_LEAF = registerItem("oolong_tea_leaf",
            new Item(new FabricItemSettings().group(GoodTea.GOOD_TEA)));
    public static final Item WHITE_TEA_LEAF = registerItem("white_tea_leaf",
            new Item(new FabricItemSettings().group(GoodTea.GOOD_TEA)));
    public static final Item YELLOW_TEA_LEAF = registerItem("yellow_tea_leaf",
            new Item(new FabricItemSettings().group(GoodTea.GOOD_TEA)));

    // Herbs and Spices
    public static final Item CLOVES = registerItem("cloves",
            new Item(new FabricItemSettings().group(GoodTea.GOOD_TEA)));
    //public static final Item GINGER_ROOT = registerItem("ginger_root",
    //        new Item(new FabricItemSettings().group(GoodTea.GOOD_TEA)));
    public static final Item GINGER_SLICE = registerItem("ginger_slice",
            new Item(new FabricItemSettings().group(GoodTea.GOOD_TEA)));
    public static final Item GRAPE_LEAF = registerItem("grape_leaf",
            new Item(new FabricItemSettings().group(GoodTea.GOOD_TEA)));
    public static final Item HONEYSUCKLE_FLOWER_BUD = registerItem("honeysuckle_flower_bud",
            new Item(new FabricItemSettings().group(GoodTea.GOOD_TEA)));
    public static final Item ROSEBUD = registerItem("rosebud",
            new Item(new FabricItemSettings().group(GoodTea.GOOD_TEA)));
    public static final Item SAGE_LEAF = registerItem("sage_leaf",
            new Item(new FabricItemSettings().group(GoodTea.GOOD_TEA)));
    public static final Item SPEARMINT_LEAF = registerItem("spearmint_leaf",
            new Item(new FabricItemSettings().group(GoodTea.GOOD_TEA)));
    //public static final Item TURMERIC_ROOT = registerItem("turmeric_root",
    //        new Item(new FabricItemSettings().group(GoodTea.GOOD_TEA)));

    // Teas
    public static final Item HONEYSUCKLE_ROSEBUD_TEA = registerItem("honeysuckle_rosebud_tea",
            new Item(new FabricItemSettings().group(GoodTea.GOOD_TEA)));
    public static final Item MASALA_CHAI_TEA = registerItem("masala_chai_tea",
            new Item(new FabricItemSettings().group(GoodTea.GOOD_TEA)));
    public static final Item OOLONG_GINGER_TEA = registerItem("oolong_ginger_tea",
            new Item(new FabricItemSettings().group(GoodTea.GOOD_TEA)));
    public static final Item SPEARMINT_TEA = registerItem("spearmint_tea",
            new Item(new FabricItemSettings().group(GoodTea.GOOD_TEA)));

    // Seeds
    public static final Item GRAPE_SEEDS = registerItem("grape_seeds",
            new AliasedBlockItem(CropsRegistry.GRAPE_VINE_BLOCK, new FabricItemSettings().group(GoodTea.GOOD_TEA)));
    public static final Item HONEYSUCKLE_SEEDS = registerItem("honeysuckle_seeds",
            new AliasedBlockItem(CropsRegistry.HONEYSUCKLE_SHRUB_BLOCK, new FabricItemSettings().group(GoodTea.GOOD_TEA)));
    public static final Item SAGE_SEEDS = registerItem("sage_seeds",
            new AliasedBlockItem(CropsRegistry.SAGE_CROP_BLOCK, new FabricItemSettings().group(GoodTea.GOOD_TEA)));
    public static final Item SPEARMINT_SEEDS = registerItem("spearmint_seeds",
            new AliasedBlockItem(CropsRegistry.SPEARMINT_CROP_BLOCK, new FabricItemSettings().group(GoodTea.GOOD_TEA)));
    public static final Item TEA_SEEDS = registerItem("tea_seeds",
            new AliasedBlockItem(CropsRegistry.TEA_BUSH_BLOCK, new FabricItemSettings().group(GoodTea.GOOD_TEA)));
    public static final Item GINGER_ROOT_PLANTABLE = registerItem("ginger_root",
            new AliasedBlockItem(CropsRegistry.GINGER_CROP_BLOCK, new FabricItemSettings().group(GoodTea.GOOD_TEA)));
    public static final Item TURMERIC_ROOT_PLANTABLE = registerItem("turmeric_root",
            new AliasedBlockItem(CropsRegistry.TURMERIC_CROP_BLOCK, new FabricItemSettings().group(GoodTea.GOOD_TEA)));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(GoodTea.MOD_ID, name), item);
    }

    public static void registerItems() {
        GoodTea.LOGGER.info("Steeping items to make some " + GoodTea.MOD_ID + "!");
    }
}
