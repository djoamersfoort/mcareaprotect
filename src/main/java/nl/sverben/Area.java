package nl.sverben;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

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

    public void Show(World world, Plugin plugin) {
        for (int x = startx ; x > stopx; x--) {
            Block block = world.getHighestBlockAt(x, startz);
            world.getBlockAt(block.getX(), block.getY() + 1, block.getZ()).setType(Material.TORCH);
            block = world.getHighestBlockAt(x, stopz);
            world.getBlockAt(block.getX(), block.getY() + 1, block.getZ()).setType(Material.TORCH);
        }
        for (int z = startz; z >= stopz; z--) {
            Block block = world.getHighestBlockAt(startx, z);
            world.getBlockAt(block.getX(), block.getY() + 1, block.getZ()).setType(Material.TORCH);
            block = world.getHighestBlockAt(stopx, z);
            world.getBlockAt(block.getX(), block.getY() + 1, block.getZ()).setType(Material.TORCH);
        }
        AreaCleaner cleaner = new AreaCleaner(this, world);
        cleaner.runTaskLater(plugin, 10*20);
    }

    public void Hide(World world) {
        for (int x = startx; x > stopx; x--) {
            Block block = world.getHighestBlockAt(x, startz);
            world.getBlockAt(block.getX(), block.getY() + 1, block.getZ()).setType(Material.AIR);
            block = world.getHighestBlockAt(x, stopz);
            world.getBlockAt(block.getX(), block.getY() + 1, block.getZ()).setType(Material.AIR);
        }
        for (int z = startz; z >= stopz; z--) {
            Block block = world.getHighestBlockAt(startx, z);
            world.getBlockAt(block.getX(), block.getY() + 1, block.getZ()).setType(Material.AIR);
            block = world.getHighestBlockAt(stopx, z);
            world.getBlockAt(block.getX(), block.getY() + 1, block.getZ()).setType(Material.AIR);
        }
    }


}
