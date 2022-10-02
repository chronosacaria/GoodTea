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
import timefall.goodtea.blocks.crops.*;
import timefall.goodtea.blocks.TeaKettleBlock;

public class BlocksRegistry {

    // Crop Blocks



    // Block Items
    public static final Block TEA_KETTLE_BLOCK = registerBlock("tea_kettle",
            new TeaKettleBlock(FabricBlockSettings.copy(Blocks.BONE_BLOCK).nonOpaque()), GoodTea.GOOD_TEA);

    private static Block registerCropBlock(String name, Item cropItem, int maxAge) {
        var block = new GoodTeaCropBlock(FabricBlockSettings.copy(Blocks.WHEAT).nonOpaque().noCollision(), cropItem, maxAge);
        return Registry.register(Registry.BLOCK, new Identifier(GoodTea.MOD_ID, name), block);
    }

    private static Block registerBlockWithoutBlockItem(String name, Block block, ItemGroup itemGroup) {
        return Registry.register(Registry.BLOCK, new Identifier(GoodTea.MOD_ID, name), block);
    }

    private static Block registerBlock(String name, Block block, ItemGroup itemGroup) {
        registerBlockItem(name, block, itemGroup);
        return Registry.register(Registry.BLOCK, new Identifier(GoodTea.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup itemGroup) {
        return Registry.register(Registry.ITEM, new Identifier(GoodTea.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(itemGroup)));
    }

    public static void registerBlocks() {
        GoodTea.LOGGER.info("Steeping blocks to make some " + GoodTea.MOD_ID + "!");
    }
}
