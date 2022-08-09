package net.maed.cinematictool.command.cinematic;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.maed.cinematictool.cinematic.Cinematic;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class CinematicCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("cinematic")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("rec")
                        .then(CommandManager.argument("name", StringArgumentType.word())
                                .executes(context -> CinematicCommand.rec(context.getSource(), StringArgumentType.getString(context, "name")))))
                .then(CommandManager.literal("play")
                        .then(CommandManager.argument("targets", EntityArgumentType.players())
                                .executes(CinematicCommand::play)))
        );
    }

    public static int rec(ServerCommandSource source, String name) throws CommandSyntaxException {
        if (Cinematic.CINEMATIC_LIST.containsKey(name)) {
            source.sendError(Text.translatable("commands.cinematic.rec.failed.cinematicalreadyexits"));
            return 0;
        }
        return 1;
    }

    private static int play(CommandContext<ServerCommandSource> context) {
        context.getSource().getPlayer().sendMessage(Text.literal("PLAY COMMAND"));
        return 0;
    }
}

