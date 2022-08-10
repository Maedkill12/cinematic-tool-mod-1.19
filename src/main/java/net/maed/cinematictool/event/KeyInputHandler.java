package net.maed.cinematictool.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.maed.cinematictool.cinematic.Cinematic;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY = "key.category.cinematictool";
    public static final String KEY_STOP = "key.cinematictool.stop";

    public static KeyBinding keyStop;

    private static void inputHandler() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyStop.wasPressed() && Cinematic.PLAYERS_RECORDING.containsKey(client.player.getUuid())) {
                long endTime = System.currentTimeMillis();
                Cinematic cinematic = Cinematic.PLAYERS_RECORDING.get(client.player.getUuid());
                cinematic.save(endTime);
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
