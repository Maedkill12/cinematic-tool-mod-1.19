package net.maed.cinematictool.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.maed.cinematictool.command.cinematic.CinematicCommand;

public class Commands {
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(CinematicCommand::register);
    }
}
