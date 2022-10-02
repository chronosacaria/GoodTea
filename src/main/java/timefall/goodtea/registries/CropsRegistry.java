package timefall.goodtea.registries;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import timefall.goodtea.GoodTea;
import timefall.goodtea.blocks.crops.GingerCropBlock;
import timefall.goodtea.blocks.crops.GoodTeaCropBlock;

public class CropsRegistry {

    public static final Block GINGER_CROP_BLOCK = registerBlockWithoutBlockItem("ginger_crop", new GingerCropBlock(FabricBlockSettings.copy(Blocks.WHEAT)));
    public static final Block GRAPE_VINE_BLOCK = registerCropBlock("grape_vine", ItemsRegistry.GRAPE_SEEDS,5);
    public static final Block HONEYSUCKLE_SHRUB_BLOCK = registerCropBlock("honeysuckle", ItemsRegistry.HONEYSUCKLE_SEEDS, 4);
    public static final Block SAGE_CROP_BLOCK = registerCropBlock("sage", ItemsRegistry.SAGE_SEEDS, 3);
    public static final Block SPEARMINT_CROP_BLOCK = registerCropBlock("spearmint", ItemsRegistry.SPEARMINT_SEEDS,3);
    public static final Block TEA_BUSH_BLOCK = registerCropBlock("tea_bush", ItemsRegistry.TEA_SEEDS, 7);
    public static final Block TURMERIC_CROP_BLOCK = registerCropBlock("turmeric", ItemsRegistry.TURMERIC_ROOT, 7);


    private static Block registerCropBlock(String name, Item cropItem, int maxAge) {
        var block = new GoodTeaCropBlock(FabricBlockSettings.copy(Blocks.WHEAT), cropItem, maxAge);
        return Registry.register(Registry.BLOCK, new Identifier(GoodTea.MOD_ID, name), block);
    }
    private static Block registerBlockWithoutBlockItem(String name, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(GoodTea.MOD_ID, name), block);
    }

    public static void registerCrops() {
        GoodTea.LOGGER.info("Steeping crop blocks to make some " + GoodTea.MOD_ID + "!");
    }
}
