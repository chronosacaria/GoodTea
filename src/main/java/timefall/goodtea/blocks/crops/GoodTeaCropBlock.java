package timefall.goodtea.blocks.crops;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.item.ItemConvertible;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;

public class GoodTeaCropBlock extends CropBlock {
    public static IntProperty CROP_AGE_7;
    public static IntProperty CROP_AGE_6;
    public static IntProperty CROP_AGE_5;
    public static IntProperty CROP_AGE_4;
    public static IntProperty CROP_AGE_3;

    static {
        CROP_AGE_7 = IntProperty.of("age", 0, 7);
        CROP_AGE_6 = IntProperty.of("age", 0, 6);
        CROP_AGE_5 = IntProperty.of("age", 0, 5);
        CROP_AGE_4 = IntProperty.of("age", 0, 4);
        CROP_AGE_3 = IntProperty.of("age", 0, 3);
    }
    int maxAge;
    ItemConvertible seedItem;

    public GoodTeaCropBlock(Settings settings, ItemConvertible seedItem, int maxAge) {
        super(settings);
        this.seedItem = seedItem;
        this.maxAge = maxAge;
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return seedItem;
    }

    @Override
    public int getMaxAge() {
        return maxAge;
    }

    @Override
    public IntProperty getAgeProperty() {
        return switch (maxAge) {
            case 7 -> CROP_AGE_7;
            case 6 -> CROP_AGE_6;
            case 5 -> CROP_AGE_5;
            case 4 -> CROP_AGE_4;
            case 3 -> CROP_AGE_3;
            default -> throw new IllegalStateException("Unexpected value: " + maxAge);
        };
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(getAgeProperty());
    }
}
