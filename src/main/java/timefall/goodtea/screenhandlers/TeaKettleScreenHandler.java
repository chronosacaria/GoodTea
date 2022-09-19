package timefall.goodtea.screenhandlers;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;
import timefall.goodtea.interfaces.IPositionedScreenHandler;
import timefall.goodtea.registries.ScreenHandlersRegistry;

public class TeaKettleScreenHandler extends Generic3x3ContainerScreenHandler implements IPositionedScreenHandler {
    private final BlockPos blockPos;

    public TeaKettleScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        super(syncId, playerInventory);
        this.blockPos = buf.readBlockPos();
    }

    public TeaKettleScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(syncId, playerInventory, inventory);
        this.blockPos = BlockPos.ORIGIN;
    }

    @Override
    public BlockPos getBlockPos() {
        return blockPos;
    }

    @Override
    public ScreenHandlerType<?> getType() {
        return ScreenHandlersRegistry.TEA_KETTLE_SCREEN_HANDLER;
    }
}
