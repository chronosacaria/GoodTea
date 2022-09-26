package timefall.goodtea.registries;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import timefall.goodtea.GoodTea;
import timefall.goodtea.blocks.entities.TeaKettleBlockEntity;

public class BlockEntitiesRegistry {
    public static BlockEntityType<TeaKettleBlockEntity> TEA_KETTLE_BLOCK_ENTITY;

    public static void registerBlockEntities() {
        TEA_KETTLE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(GoodTea.MOD_ID, "tea_kettle_block_entity"),
                FabricBlockEntityTypeBuilder.create(TeaKettleBlockEntity::new,
                        BlocksRegistry.TEA_KETTLE_BLOCK).build(null));
        FluidStorage.SIDED.registerForBlockEntity(
                (blockEntity, direction) -> blockEntity.fluidStorage, TEA_KETTLE_BLOCK_ENTITY);
    }
}
