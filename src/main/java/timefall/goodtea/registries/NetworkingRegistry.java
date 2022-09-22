package timefall.goodtea.registries;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import timefall.goodtea.GoodTea;
import timefall.goodtea.networking.FluidSyncS2CPacket;

public class NetworkingRegistry {
    public static final Identifier WATER_SYNC = new Identifier(GoodTea.MOD_ID, "water_sync");

    public static void registerC2SPackets() {

    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(WATER_SYNC, FluidSyncS2CPacket::receive);
    }
}
