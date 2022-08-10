package net.maed.cinematictool;

import net.fabricmc.api.ClientModInitializer;
import net.maed.cinematictool.event.KeyInputHandler;

public class CinematicToolClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyInputHandler.registerKeyBinding();
    }
}
