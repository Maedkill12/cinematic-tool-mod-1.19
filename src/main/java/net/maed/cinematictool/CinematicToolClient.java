package net.maed.cinematictool;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.maed.cinematictool.event.KeyInputHandler;
import net.maed.cinematictool.event.ModRenderCallback;
import net.maed.cinematictool.networking.Packet;

public class CinematicToolClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Packet.registerS2CPackets();
        KeyInputHandler.registerKeyBinding();

        HudRenderCallback.EVENT.register(new ModRenderCallback());
    }
}
