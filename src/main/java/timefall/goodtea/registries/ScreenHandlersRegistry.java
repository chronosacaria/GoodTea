package timefall.goodtea.registries;

import net.minecraft.screen.ScreenHandlerType;
import timefall.goodtea.screens.screenhandlers.TeaKettleScreenHandler;

public class ScreenHandlersRegistry {
    public static ScreenHandlerType<TeaKettleScreenHandler> TEA_KETTLE_SCREEN_HANDLER;

    public static void registerScreenHandlers() {
        TEA_KETTLE_SCREEN_HANDLER = new ScreenHandlerType<>(TeaKettleScreenHandler::new);
    }
}
