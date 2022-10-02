package timefall.goodtea.blocks.crops;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.item.ItemConvertible;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;

public class GoodTeaCropBlockAge5 extends CropBlock {

    public static final IntProperty AGE = Properties.AGE_5;
    public static final int MAX_AGE = Properties.AGE_5_MAX;
    ItemConvertible seedItem;

    public GoodTeaCropBlockAge5(Settings settings, ItemConvertible seedItem) {
        super(settings);
        this.seedItem = seedItem;
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return seedItem;
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    public IntProperty getAgeProperty() {
        return AGE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}
