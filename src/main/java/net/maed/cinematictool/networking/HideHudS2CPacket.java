package net.maed.cinematictool.networking;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.maed.cinematictool.cinematic.Cinematic;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class HideHudS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender packetSender) {
        boolean hidden = buf.readBoolean();
        //client.options.hudHidden = hidden;
        if (hidden) {
            Cinematic.PLAYERS_IN_CINEMATIC.add(client.player.getUuid());
        } else {
            Cinematic.PLAYERS_IN_CINEMATIC.remove(client.player.getUuid());
        }
    }
}
