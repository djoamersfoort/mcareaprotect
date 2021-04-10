package nl.sverben;

import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class AreaCleaner extends BukkitRunnable {
    private final Area area;
    private final World world;

    public AreaCleaner(Area area, World world) {
        this.area = area;
        this.world = world;
    }

    @Override
    public void run() {
        this.area.Hide(world);
    }
}
