package net.maed.cinematictool.command.cinematic;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.maed.cinematictool.cinematic.Cinematic;
import net.maed.cinematictool.networking.Packet;
import net.maed.cinematictool.util.CinematicUtil;
import net.maed.cinematictool.util.Location;
import net.maed.cinematictool.util.PlayerUtil;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CinematicCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("cinematic")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("record")
                        .then(CommandManager.argument("name", StringArgumentType.word())
                                .executes(context -> CinematicCommand.rec(context.getSource(), StringArgumentType.getString(context, "name")))))
                .then(CommandManager.literal("play")
                        .then(CommandManager.argument("targets", EntityArgumentType.entities())
                                .then(CommandManager.argument("name", StringArgumentType.word()).suggests(new CinematicSuggestionProvider())
                                        .executes(context -> CinematicCommand.play(context.getSource(), EntityArgumentType.getPlayers(context, "targets"), StringArgumentType.getString(context, "name"))))))
        );
    }

    public static int rec(ServerCommandSource source, String name) {
        if (Cinematic.CINEMATIC_LIST.containsKey(name)) {
            source.sendError(Text.translatable("commands.cinematic.rec.failed.cinematicalreadyexits"));
            return 0;
        }
        PlayerEntity player = source.getPlayer();
        if (player == null) {
            source.sendError(Text.translatable("commands.cinematic.rec.failed.playernull"));
            return 0;
        }

        if (Cinematic.PLAYERS_RECORDING.containsKey(player.getUuid())) {
            source.sendError(Text.translatable("commands.cinematic.rec.failed.playerrecording"));
            return 0;
        }

        ServerPlayNetworking.send((ServerPlayerEntity) player, Packet.SEND_MESSAGE, PacketByteBufs.create());

        Cinematic cinematic = new Cinematic(name, player.getWorld().getRegistryKey(), player);
        Cinematic.PLAYERS_RECORDING.put(player.getUuid(), cinematic);
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        AtomicInteger startTime = new AtomicInteger();

        long startTimeMillisecond = System.currentTimeMillis();
        cinematic.setStartTime(startTimeMillisecond);
        ScheduledFuture<?> recCinematic = executorService.scheduleAtFixedRate(() -> {
            CinematicCommand.onRecording(cinematic, player , startTime.get());
            startTime.getAndIncrement();
        }, 0, 16, TimeUnit.MILLISECONDS);

        cinematic.setRecCinematic(recCinematic);

        return 1;
    }

    private static int play(ServerCommandSource source, Collection<ServerPlayerEntity> targets, String name) {
        if (!Cinematic.CINEMATIC_LIST.containsKey(name)) {
            source.sendError(Text.translatable("commands.cinematic.rec.failed.cinematicnotexist"));
            return 0;
        }

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        Cinematic cinematic = Cinematic.CINEMATIC_LIST.get(name);

        Collection<Location> locations = cinematic.getLocations();
        Iterator<Location> i = locations.iterator();

        Collection<ServerPlayerEntity> availablePlayers = Cinematic.getAvailablePlayers(targets);
        Cinematic.addPlayers(availablePlayers);

        AtomicInteger test = new AtomicInteger();

        ScheduledFuture<?> playExecutor = executorService.scheduleAtFixedRate(() -> {
            try {
                test.getAndIncrement();
                Location location = i.next();
                for (ServerPlayerEntity player : availablePlayers) {
                    onPlaying(cinematic, player, location);
                }
            } catch (NoSuchElementException e) {

            }
        }, 0, 16, TimeUnit.MILLISECONDS);

        executorService.schedule(() -> {
            onStopped(cinematic, source, availablePlayers);
            playExecutor.cancel(false);
        }, cinematic.getSeconds(), TimeUnit.SECONDS);
        return 1;
    }

    private static void onRecording(Cinematic cinematic, PlayerEntity player, int time) {
        player.sendMessage(Text.literal("RECORDING " + Math.round( time/62.5)), true);

        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();
        float pitch = player.getPitch();
        float yaw = player.getYaw();
        cinematic.addLocation(new Location(x, y, z, pitch, yaw));
    }
    public static void onSaved(Cinematic cinematic, PlayerEntity player) {
        CinematicUtil.saveCinematic(cinematic);
        Cinematic.CINEMATIC_LIST.put(cinematic.getName(), cinematic);
        Cinematic.PLAYERS_RECORDING.remove(player.getUuid());
        player.sendMessage(Text.literal("SAVED"));
    }

    private static void onPlaying(Cinematic cinematic, ServerPlayerEntity player, Location location) {
        PlayerUtil.teleport(player, player.getServer().getWorld(cinematic.getDimension()), location);
    }

    private static void onStopped(Cinematic cinematic, ServerCommandSource source, Collection<ServerPlayerEntity> targets) {
        Cinematic.removePlayers(targets);
        source.sendFeedback(Text.literal(cinematic.getName() + " finished"), false);
    }
}

