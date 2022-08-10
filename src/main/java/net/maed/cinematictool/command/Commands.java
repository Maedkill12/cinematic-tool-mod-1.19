package net.maed.cinematictool.command;

import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.maed.cinematictool.CinematicTool;
import net.maed.cinematictool.command.cinematic.CinematicCommand;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.Identifier;

public class Commands {
    public static void registerCommands() {
        ArgumentTypeRegistry.registerArgumentType(new Identifier(CinematicTool.MOD_ID, "cinematic"), CinematicArgumentType.class, ConstantArgumentSerializer.of(CinematicArgumentType::cinematic));
        CommandRegistrationCallback.EVENT.register(CinematicCommand::register);
    }
}
