package net.maed.cinematictool.networking;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.maed.cinematictool.cinematic.Cinematic;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class CinematicSaveC2SPacket {

    public static void receive(MinecraftServer minecraftServer, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender packetSender) {
        long endTime = buf.readLong();
        if (Cinematic.PLAYERS_RECORDING.containsKey(player.getUuid())) {
            Cinematic cinematic = Cinematic.PLAYERS_RECORDING.get(player.getUuid());
            cinematic.save(endTime);
        }
    }
}
