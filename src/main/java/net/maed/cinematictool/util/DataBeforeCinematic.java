package net.maed.cinematictool.util;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameMode;

public class DataBeforeCinematic {
    private  Location location;
    private ServerWorld world;
    private GameMode gameMode;

    public DataBeforeCinematic(ServerWorld world) {
        this.world = world;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ServerWorld getWorld() {
        return world;
    }


}
