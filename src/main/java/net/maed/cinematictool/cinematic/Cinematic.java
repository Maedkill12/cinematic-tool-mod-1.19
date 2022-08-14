package net.maed.cinematictool.cinematic;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.maed.cinematictool.command.cinematic.CinematicCommand;
import net.maed.cinematictool.networking.Packet;
import net.maed.cinematictool.util.DataBeforeCinematic;
import net.maed.cinematictool.util.Location;
import net.maed.cinematictool.util.ModTeam;
import net.maed.cinematictool.util.PlayerUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

public class Cinematic {
    public static final HashMap<String, Cinematic> CINEMATIC_LIST = new HashMap<>();
    public static final Set<UUID> PLAYERS_IN_CINEMATIC = new HashSet<>();
    public static final HashMap<UUID, Cinematic> PLAYERS_RECORDING = new HashMap<>();
    public static final HashMap<UUID, DataBeforeCinematic> DATA = new HashMap<>();

    private String name;
    private ArrayList<Location> locations;
    private RegistryKey<World> dimension;
    private long startTime;
    private long seconds;
    private ScheduledFuture<?> recCinematic;
    private PlayerEntity cameraGuy;

    public Cinematic(String name, RegistryKey<World> dimension, PlayerEntity cameraGuy) {
        this.name = name;
        this.dimension = dimension;
        this.cameraGuy = cameraGuy;
        this.locations = new ArrayList<>();
    }

    public void addLocation(Location location) {
        locations.add(location);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<Location> locations) {
        this.locations = locations;
    }

    public RegistryKey<World> getDimension() {
        return dimension;
    }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setRecCinematic(ScheduledFuture<?> recCinematic) {
        this.recCinematic = recCinematic;
    }

    public void save(long endTime) {
        long seconds = (endTime - startTime) / 1000;
        this.setSeconds(seconds);
        this.recCinematic.cancel(false);
        CinematicCommand.onSaved(this, cameraGuy);
    }

    public JsonObject getJsonFormat() {
        JsonObject cinematic = new JsonObject();
        cinematic.addProperty("world", this.getDimension().getValue().toString());
        cinematic.addProperty("camera", this.cameraGuy.getUuidAsString());
        cinematic.addProperty("seconds", this.getSeconds());

        JsonObject jsonLocations = new JsonObject();
        for (int i = 0; i < locations.size(); i++) {
            Location l = locations.get(i);
            JsonObject location = new JsonObject();
            location.addProperty("x", l.getX());
            location.addProperty("y", l.getY());
            location.addProperty("z", l.getZ());
            location.addProperty("yaw", l.getYaw());
            location.addProperty("pitch", l.getPitch());

            jsonLocations.add("" + i, location);
        }

        cinematic.add("frames", jsonLocations);

        return cinematic;
    }

    public PlayerEntity getCameraGuy() {
        return cameraGuy;
    }

    public static Collection<ServerPlayerEntity> getAvailablePlayers(Collection<ServerPlayerEntity> targets) {
        return targets.stream().filter(player -> !PLAYERS_IN_CINEMATIC.contains(player.getUuid()) && !PLAYERS_RECORDING.containsKey(player.getUuid())).collect(Collectors.toList()) ;
    }

    public static void addPlayers(Collection<ServerPlayerEntity> targets) {
        targets.forEach(player -> {
            playerCinematicModeOn(player);
            PLAYERS_IN_CINEMATIC.add(player.getUuid());
        });
    }

    public static void removePlayers(Collection<ServerPlayerEntity> targets) {
        targets.forEach(player -> {
            playerCinematicModeOff(player);
            PLAYERS_IN_CINEMATIC.remove(player.getUuid());
        });
    }

    private static void playerCinematicModeOn(ServerPlayerEntity player) {
        player.getServer().getScoreboard().addPlayerToTeam(player.getDisplayName().getString(), ModTeam.getTeam(player.getServer()));

        Location location = new Location(player.getX(), player.getY(), player.getZ(), player.getPitch(), player.getYaw());

        DataBeforeCinematic data = new DataBeforeCinematic(player.getWorld());
        data.setLocation(location);
        data.setGameMode(player.interactionManager.getGameMode());

        DATA.put(player.getUuid(), data);

        player.changeGameMode(GameMode.SPECTATOR);
        updateHud(player, true);
    }

    private static void playerCinematicModeOff(ServerPlayerEntity player) {
        player.getServer().getScoreboard().removePlayerFromTeam(player.getDisplayName().getString(), ModTeam.getTeam(player.getServer()));

        DataBeforeCinematic dataBeforeCinematic = DATA.get(player.getUuid());
        player.changeGameMode(dataBeforeCinematic.getGameMode());

        PlayerUtil.teleport(player, dataBeforeCinematic.getWorld(), dataBeforeCinematic.getLocation());
        DATA.remove(player.getUuid());
        updateHud(player, false);
    }

    private static void updateHud(ServerPlayerEntity player, boolean hudHidden) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(hudHidden);
        ServerPlayNetworking.send(player, Packet.HIDE_CLIENT_ID, buf);
    }
}
