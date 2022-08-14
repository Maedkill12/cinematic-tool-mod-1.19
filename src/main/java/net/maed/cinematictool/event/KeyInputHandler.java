package net.maed.cinematictool.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.maed.cinematictool.networking.Packet;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY = "key.category.cinematictool";
    public static final String KEY_STOP = "key.cinematictool.stop";

    public static KeyBinding keyStop;

    private static void inputHandler() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyStop.wasPressed()) {
                long endTime = System.currentTimeMillis();
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeLong(endTime);
                ClientPlayNetworking.send(Packet.CINEMATIC_SAVE_ID, buf);
            }
        });
    }

    public static void registerKeyBinding() {
        keyStop = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_STOP,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                KEY_CATEGORY
        ));
        inputHandler();
    }
}
