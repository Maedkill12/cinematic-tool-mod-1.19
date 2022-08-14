package net.maed.cinematictool;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.maed.cinematictool.command.Commands;
import net.maed.cinematictool.event.ServerStartedListener;
import net.maed.cinematictool.networking.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CinematicTool implements ModInitializer {
	public static final String MOD_ID = "cinematictool";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Packet.registerC2SPackets();
		Commands.registerCommands();
		ServerLifecycleEvents.SERVER_STARTED.register(new ServerStartedListener());
	}
}
