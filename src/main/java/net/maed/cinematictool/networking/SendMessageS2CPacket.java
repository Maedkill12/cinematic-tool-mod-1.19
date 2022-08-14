package net.maed.cinematictool.networking;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.maed.cinematictool.event.KeyInputHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class SendMessageS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender packetSender) {
        client.player.sendMessage(Text.translatable("commands.cinematic.rec.infostop", Text.keybind(KeyInputHandler.keyStop.getTranslationKey())));
    }
}
