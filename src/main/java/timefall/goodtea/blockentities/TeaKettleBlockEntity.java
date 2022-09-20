package timefall.goodtea.blockentities;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import timefall.goodtea.handlers.ItemStackHandler;
import timefall.goodtea.registries.BlockEntitiesRegistry;
import timefall.goodtea.screens.screenhandlers.TeaKettleScreenHandler;

public class TeaKettleBlockEntity extends LootableContainerBlockEntity implements ExtendedScreenHandlerFactory {
    private DefaultedList<ItemStack> items = DefaultedList.ofSize(size(), ItemStack.EMPTY);

    public TeaKettleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntitiesRegistry.TEA_KETTLE_BLOCK_ENTITY, blockPos, blockState);
    }

    @Override
    protected DefaultedList<ItemStack> getInvStackList() {
        return items;
    }

    @Override
    protected void setInvStackList(DefaultedList<ItemStack> list) {
        this.items = list;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new TeaKettleScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public ItemStackHandler getInventory() {
        return itemHandler;
    }
}
