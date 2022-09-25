package timefall.goodtea.registries;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import timefall.goodtea.GoodTea;
import timefall.goodtea.networking.FluidSyncS2CPacket;
import timefall.goodtea.networking.ItemStackSyncS2CPacket;

public class PacketsRegistry {
    public static final Identifier FLUID_SYNC = new Identifier(GoodTea.MOD_ID, "fluid_sync");
    public static final Identifier ITEM_SYNC = new Identifier(GoodTea.MOD_ID, "item_sync");

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(FLUID_SYNC, FluidSyncS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(ITEM_SYNC, ItemStackSyncS2CPacket::receive);
    }
}
