package net.maed.cinematictool;

import net.fabricmc.api.ModInitializer;
import net.maed.cinematictool.command.Commands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CinematicTool implements ModInitializer {
	public static final String MOD_ID = "cinematictool";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Commands.registerCommands();
	}
}
