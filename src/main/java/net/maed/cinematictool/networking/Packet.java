package net.maed.cinematictool.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.maed.cinematictool.CinematicTool;
import net.minecraft.util.Identifier;

public class Packet {
    public static final Identifier CINEMATIC_SAVE_ID = new Identifier(CinematicTool.MOD_ID, "cinematic_save");
    public static final Identifier HIDE_CLIENT_ID = new Identifier(CinematicTool.MOD_ID, "hide_client");
    public static final Identifier SEND_MESSAGE = new Identifier(CinematicTool.MOD_ID, "send_message");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(CINEMATIC_SAVE_ID, CinematicSaveC2SPacket::receive);
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(HIDE_CLIENT_ID, HideHudS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(SEND_MESSAGE, SendMessageS2CPacket::receive);
    }
}
