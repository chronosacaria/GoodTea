package timefall.goodtea.registries;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import timefall.goodtea.GoodTea;
import timefall.goodtea.screenhandlers.TeaKettleScreenHandler;

public class ScreenHandlersRegistry {
    public static final ScreenHandlerType<TeaKettleScreenHandler> TEA_KETTLE_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(TeaKettleScreenHandler::new);

    public static void registerScreenHandlers() {
        Registry.register(Registry.SCREEN_HANDLER, new Identifier(GoodTea.MOD_ID, "tea_kettle"), TEA_KETTLE_SCREEN_HANDLER);
    }
}
