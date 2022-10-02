package timefall.goodtea.registries;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import timefall.goodtea.GoodTea;
import timefall.goodtea.blocks.crops.GoodTeaCropBlockAge3;
import timefall.goodtea.blocks.crops.GoodTeaCropBlockAge5;
import timefall.goodtea.blocks.crops.GoodTeaCropBlockAge7;

public class CropsRegistry {

    public static final Block GINGER_CROP_BLOCK = registerBlockWithoutBlockItem("ginger_crop", new GoodTeaCropBlockAge7(FabricBlockSettings.copy(Blocks.WHEAT), ItemsRegistry.GINGER_ROOT_PLANTABLE));
    public static final Block GRAPE_VINE_BLOCK = registerBlockWithoutBlockItem("grape_vine", new GoodTeaCropBlockAge5(FabricBlockSettings.copy(Blocks.WHEAT), ItemsRegistry.GRAPE_SEEDS));
    // Should be a bush
    public static final Block HONEYSUCKLE_SHRUB_BLOCK = registerBlockWithoutBlockItem("honeysuckle", new GoodTeaCropBlockAge5(FabricBlockSettings.copy(Blocks.WHEAT), ItemsRegistry.HONEYSUCKLE_SEEDS));
    public static final Block SAGE_CROP_BLOCK = registerBlockWithoutBlockItem("sage", new GoodTeaCropBlockAge3(FabricBlockSettings.copy(Blocks.WHEAT), ItemsRegistry.SAGE_SEEDS));
    public static final Block SPEARMINT_CROP_BLOCK = registerBlockWithoutBlockItem("spearmint", new GoodTeaCropBlockAge3(FabricBlockSettings.copy(Blocks.WHEAT), ItemsRegistry.SPEARMINT_SEEDS));
    public static final Block TEA_BUSH_BLOCK = registerBlockWithoutBlockItem("tea_bush", new GoodTeaCropBlockAge7(FabricBlockSettings.copy(Blocks.WHEAT), ItemsRegistry.TEA_SEEDS));
    public static final Block TURMERIC_CROP_BLOCK = registerBlockWithoutBlockItem("turmeric", new GoodTeaCropBlockAge7(FabricBlockSettings.copy(Blocks.WHEAT), ItemsRegistry.TURMERIC_ROOT_PLANTABLE));

    private static Block registerBlockWithoutBlockItem(String name, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(GoodTea.MOD_ID, name), block);
    }

    public static void registerCrops() {
        GoodTea.LOGGER.info("Steeping crop blocks to make some " + GoodTea.MOD_ID + "!");
    }
}
