package timefall.goodtea.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.Generic3x3ContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import timefall.goodtea.GoodTea;
import timefall.goodtea.registries.BlocksRegistry;
import timefall.goodtea.registries.ScreenHandlersRegistry;

@Environment(net.fabricmc.api.EnvType.CLIENT)
public class GoodTeaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ScreenHandlersRegistry.TEA_KETTLE, Generic3x3ContainerScreen::new);

        BlockRenderLayerMap.INSTANCE.putBlock(BlocksRegistry.SPEARMINT_BLOCK, RenderLayer.getCutout());
    }
}
