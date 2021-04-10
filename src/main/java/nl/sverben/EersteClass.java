package nl.sverben;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class EersteClass extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        Logger logger = getLogger();
        logger.info("Areaprotect Enabled");
        getServer().getPluginManager().registerEvents(this, this);
        try {
            configReload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Location location = block.getLocation();

        for(Area area: areas) {
            if (area.Overlaps(location)) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Location location = block.getLocation();

        for(Area area: areas) {
            if (area.Overlaps(location)) event.setCancelled(true);
        }
    }

    @EventHandler
    public void entity(EntityChangeBlockEvent event) {
        Block block = event.getBlock();
        Location location = block.getLocation();

        for(Area area: areas) {
            if (area.Overlaps(location)) event.setCancelled(true);
        }
    }

    @EventHandler
    public void explosion2(EntityExplodeEvent event) {
        Location location = event.getLocation();

        for(Area area: areas) {
            if (area.Overlaps(location)) event.setCancelled(true);
        }
    }

    private ArrayList<Area> areas = new ArrayList<Area>();
    private void configReload() throws IOException {
        FileReader filereader;
        try {
            filereader = new FileReader("positions.txt");
        } catch (IOException e){
            areas.clear();
            return;
        }
        BufferedReader bufferedreader = new BufferedReader(filereader);
        String line;
        areas.clear();
        while((line=bufferedreader.readLine())!=null){
            String[] array = line.split(";");
            int startx = Integer.parseInt(array[0]);
            int stopx = Integer.parseInt(array[1]);
            int startz = Integer.parseInt(array[2]);
            int stopz = Integer.parseInt(array[3]);
            Area area = new Area(startx, stopx, startz, stopz);
            areas.add(area);
        }
        filereader.close();
    }

    private void writeCoordinates(int startx, int startz, int stopx, int stopz) {
        StringBuilder builder = new StringBuilder();
        if (startx <= stopx){
            int oldstartx = startx;
            startx = stopx;
            stopx = oldstartx;
        }
        if (startz <= stopz) {
            int oldstartz = startz;
            startz = stopz;
            stopz = oldstartz;
        }
        builder.append(startx);
        builder.append(";");
        builder.append(stopx);
        builder.append(";");
        builder.append(startz);
        builder.append(";");
        builder.append(stopz);
        builder.append("\n");
        try {
            FileWriter writer = new FileWriter("positions.txt", true);
            writer.write(String.valueOf(builder));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int startx =  0;
    int startz = 0;
    @EventHandler
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("rstart")){
                if (!player.isOp()) {
                    player.sendMessage(ChatColor.RED + "You are not op");
                    return false;
                }
                Location location = player.getLocation();
                startx = (int) Math.round(location.getX());
                startz = (int) Math.round(location.getZ());
                player.sendMessage(ChatColor.RED + "#Selection " + ChatColor.DARK_RED + "First position selected");
            }

            else if (cmd.getName().equalsIgnoreCase("rstop")){
                if (!player.isOp()) {
                    player.sendMessage(ChatColor.RED + "You are not op");
                    return false;
                }
                Location location = player.getLocation();
                int stopx = (int) Math.round(location.getX());
                int stopz = (int) Math.round(location.getZ());
                player.sendMessage(ChatColor.RED + "#Selection " + ChatColor.DARK_RED + "Second position selected");
                writeCoordinates(startx, startz, stopx, stopz);
                try {
                    configReload();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            else if (cmd.getName().equalsIgnoreCase("rclear")){
                if (!player.isOp()) {
                    player.sendMessage(ChatColor.RED + "You are not op");
                    return false;
                }
                areas.clear();
                File f= new File("positions.txt");
                f.delete();
            }

            else if (cmd.getName().equalsIgnoreCase("rcarea")){
                if (!player.isOp()) {
                    player.sendMessage(ChatColor.RED + "You are not op");
                    return false;
                }
                areas.removeIf(area -> area.Overlaps(player.getLocation()));
                File f= new File("positions.txt");
                f.delete();

                for (Area area: areas){
                    writeCoordinates(area.startx, area.startz, area.stopx, area.stopz);
                }
                try {
                    configReload();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            else if (cmd.getName().equalsIgnoreCase("rpause")){
                if (!player.isOp()) {
                    player.sendMessage(ChatColor.RED + "You are not op");
                    return false;
                }
                areas.clear();
            } else if (cmd.getName().equalsIgnoreCase("runpause")) {
                if (!player.isOp()) {
                    player.sendMessage(ChatColor.RED + "You are not op");
                    return false;
                }
                try {
                    configReload();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (cmd.getName().equalsIgnoreCase("rshow")){
                if (!player.isOp()) {
                    player.sendMessage(ChatColor.RED + "You are not op");
                    return false;
                }
                for(Area area: areas) {
                    area.Show(player.getWorld(), this);
                }

            }
        }
    return true;
    }
}
