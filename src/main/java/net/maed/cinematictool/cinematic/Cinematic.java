package net.maed.cinematictool.cinematic;

import net.maed.cinematictool.command.cinematic.CinematicCommand;
import net.maed.cinematictool.util.Location;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

public class Cinematic {
    public static final HashMap<String, Cinematic> CINEMATIC_LIST = new HashMap<>();
    public static final Set<UUID> PLAYERS_IN_CINEMATIC = new HashSet<>();
    public static final HashMap<UUID, Cinematic> PLAYERS_RECORDING = new HashMap<>();

    private String name;
    private ArrayList<Location> locations;
    private ServerWorld world;
    private long startTime;
    private long seconds;
    private ScheduledFuture<?> recCinematic;
    private PlayerEntity recorder;

    public Cinematic(String name, ServerWorld world, PlayerEntity recorder) {
        this.name = name;
        this.world = world;
        this.recorder = recorder;
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

    public ServerWorld getWorld() {
        return world;
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
        CinematicCommand.onSaved(this, recorder);
    }

    public static Collection<ServerPlayerEntity> getAvailablePlayers(Collection<ServerPlayerEntity> targets) {
        return targets.stream().filter(player -> !PLAYERS_IN_CINEMATIC.contains(player.getUuid()) && !PLAYERS_RECORDING.containsKey(player.getUuid())).collect(Collectors.toList()) ;
    }

    public static void removePlayers(Collection<ServerPlayerEntity> targets) {
        targets.forEach(player -> PLAYERS_IN_CINEMATIC.remove(player.getUuid()));
    }

    public static void addPlayers(Collection<ServerPlayerEntity> targets) {
        targets.forEach(player -> PLAYERS_IN_CINEMATIC.add(player.getUuid()));
    }
}
