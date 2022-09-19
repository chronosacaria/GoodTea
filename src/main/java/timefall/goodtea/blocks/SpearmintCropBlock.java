package timefall.goodtea.blocks;

import net.minecraft.block.CropBlock;
import net.minecraft.item.ItemConvertible;
import timefall.goodtea.registries.ItemsRegistry;

public class SpearmintCropBlock extends CropBlock {
    public SpearmintCropBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return ItemsRegistry.SPEARMINT_SEEDS;
    }
}
