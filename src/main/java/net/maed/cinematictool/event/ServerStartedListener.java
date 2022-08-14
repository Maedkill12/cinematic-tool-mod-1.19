package net.maed.cinematictool.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.maed.cinematictool.util.CinematicUtil;
import net.minecraft.server.MinecraftServer;

public class ServerStartedListener implements ServerLifecycleEvents.ServerStarted {
    @Override
    public void onServerStarted(MinecraftServer server) {
        CinematicUtil.loadCinematic(server);
    }
}
