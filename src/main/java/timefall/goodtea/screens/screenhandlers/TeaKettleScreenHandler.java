package timefall.goodtea.screens.screenhandlers;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;
import timefall.goodtea.blockentities.TeaKettleBlockEntity;
import timefall.goodtea.handlers.ItemStackHandler;
import timefall.goodtea.handlers.SlotItemHandler;
import timefall.goodtea.registries.ExtendedScreenTypesRegistry;

public class TeaKettleScreenHandler extends ScreenHandler {

    private static final int INV_INDEX_INGREDIENT_DISPLAY = 9;
    private static final int INV_INDEX_CONTAINER_INPUT = INV_INDEX_INGREDIENT_DISPLAY + 1;
    private static final int INV_INDEX_OUTPUT = INV_INDEX_CONTAINER_INPUT + 1;
    private static final int INV_INDEX_START_PLAYER_INVENTORY = INV_INDEX_OUTPUT + 1;
    private static final int INV_INDEX_END_PLAYER_INVENTORY = INV_INDEX_START_PLAYER_INVENTORY + 36;

    public final TeaKettleBlockEntity teaKettleBlockEntity;
    public final ItemStackHandler inventoryHandler;
    private final PropertyDelegate teaKettleData;
    private final ScreenHandlerContext canInteractWithCallable;

    public TeaKettleScreenHandler(final int windowId, final PlayerInventory playerInventory, final TeaKettleBlockEntity teaKettleBlockEntity, PropertyDelegate teaKettleData) {
        super(ExtendedScreenTypesRegistry.TEA_KETTLE.get(), windowId);
        this.teaKettleBlockEntity = teaKettleBlockEntity;
        this.inventoryHandler = teaKettleBlockEntity.getInventory();
        this.teaKettleData = teaKettleData;
        this.canInteractWithCallable = ScreenHandlerContext.create(teaKettleBlockEntity.getWorld(), teaKettleBlockEntity.getPos());

        // Ingredient Slots 3 Rows x 3 Columns
        int startX = 8;
        int startY = 18;
        int inputStartX = 30;
        int inputStartY = 17;
        int borderSlotSize = 18;
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 3; ++column) {
                addSlot(new SlotItemHandler(inventoryHandler, (row * 3) + column,
                        inputStartX + (column * borderSlotSize),
                        inputStartY + (row * borderSlotSize)));
            }
        }

        // Tea Slot
        addSlot(new SlotItemHandler(inventoryHandler, 10, 124, 26));
    }

}

