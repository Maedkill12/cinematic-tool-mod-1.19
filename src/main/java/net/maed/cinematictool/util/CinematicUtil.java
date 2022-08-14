package net.maed.cinematictool.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.maed.cinematictool.cinematic.Cinematic;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.UUID;

public class CinematicUtil {
    public static void loadCinematic(MinecraftServer server) {
        Path path = FabricLoader.getInstance().getConfigDir();
        File file = path.resolve("cinematic").toFile();

        if (!file.exists()) {
            file.mkdir();
        }

        String names[] = file.list();

        for (String name: names) {
            loadCinematic(server, name);
        }
    }

    public static void saveCinematic(Cinematic cinematic) {
        Path path = FabricLoader.getInstance().getConfigDir();
        Path cinematicPath = path.resolve("cinematic");
        File file = cinematicPath.resolve(cinematic.getName() + ".json").toFile();
        try {
            if (file.length() == 0) {
                createFile(file);
            }
            JsonParser parser = new JsonParser();
            JsonObject root = (JsonObject) parser.parse(new FileReader(file.getPath()));
            root.add(cinematic.getName(), cinematic.getJsonFormat());

            FileWriter fileWriter = new FileWriter(file.getPath());
            fileWriter.write(root.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createFile(File file) throws IOException {
        FileWriter fileWriter = new FileWriter(file.getPath());
        fileWriter.write("{}");
        fileWriter.close();
    }

    private static void loadCinematic(MinecraftServer server, String nameFile) {
        Path path = FabricLoader.getInstance().getConfigDir();
        Path cinematicPath = path.resolve("cinematic");
        File file = cinematicPath.resolve(nameFile).toFile();

        JsonParser parser = new JsonParser();
        try {
            if (file.length() == 0) {
                return;
            }
            JsonObject root = (JsonObject) parser.parse(new FileReader(file.getPath()));
            JsonObject jsonObject = root.getAsJsonObject();
            jsonObject.keySet().forEach(name -> {
                JsonObject jsonCinematic = (JsonObject) jsonObject.get(name);

                String cinematicDimension = jsonCinematic.get("world").toString().replaceAll("\"", "");
                String camera = jsonCinematic.get("camera").toString().replaceAll("\"", "");
                long seconds = Long.parseLong(jsonCinematic.get("seconds").toString().replaceAll("\"", ""));
                JsonObject frames = (JsonObject) jsonCinematic.get("frames");

                RegistryKey<World> dimension = RegistryKey.of(Registry.WORLD_KEY, new Identifier(cinematicDimension));

                ArrayList<Location> locations = new ArrayList<>();
                frames.keySet().forEach(frame -> {
                    JsonObject currentFrame = (JsonObject) frames.get(frame);

                    double x = Double.parseDouble(currentFrame.get("x").toString().replaceAll("\"", ""));
                    double y = Double.parseDouble(currentFrame.get("y").toString().replaceAll("\"", ""));
                    double z = Double.parseDouble(currentFrame.get("z").toString().replaceAll("\"", ""));
                    float yaw = Float.parseFloat(currentFrame.get("yaw").toString().replaceAll("\"", ""));
                    float pitch = Float.parseFloat(currentFrame.get("pitch").toString().replaceAll("\"", ""));

                    locations.add(new Location(x, y, z, pitch, yaw));


                });

                Cinematic cinematic = new Cinematic(name, dimension, server.getPlayerManager().getPlayer(UUID.fromString(camera)));
                cinematic.setSeconds(seconds);
                cinematic.setLocations(locations);

                Cinematic.CINEMATIC_LIST.put(name, cinematic);
            });


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
