package net.maed.cinematictool.util;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class PlayerUtil {
    public static void teleport(ServerPlayerEntity player, ServerWorld world, Location location) {
        player.teleport(world, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }
}
