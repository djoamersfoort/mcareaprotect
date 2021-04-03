package nl.sverben;

import org.bukkit.Location;

import java.lang.reflect.Constructor;

public class Area {

    public Area(int startx, int stopx, int startz, int stopz){
        this.startx = startx;
        this.startz = startz;
        this.stopx = stopx;
        this.stopz = stopz;
    }
    public int startx;
    public int startz;
    public int stopx;
    public int stopz;

    public boolean Overlaps(Location location) {
        if (location.getBlockX() <= startx && location.getBlockX() >= stopx && location.getBlockZ() <= startz && location.getBlockZ() >= stopz) {
            return true;
        }
        return false;
    }


}
