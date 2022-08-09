package net.maed.cinematictool.cinematic;

import net.maed.cinematictool.util.Location;

import java.util.ArrayList;
import java.util.HashMap;

public class Cinematic {
    public static final HashMap<String, Cinematic> CINEMATIC_LIST = new HashMap<>();

    private String name;
    private ArrayList<Location> locations;

    public Cinematic(String name) {
        this.name = name;
        this.locations = new ArrayList<>();
    }

    public void addLocation(Location location) {
        locations.add(location);
    }
}
