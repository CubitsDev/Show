package network.palace.show.commands.showgen;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import network.palace.show.ShowPlugin;
import network.palace.show.actions.FakeBlockAction;
import network.palace.show.generator.GeneratorSession;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GenerateCommand {

    public void handle(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        GeneratorSession session = ShowPlugin.getShowGenerator().getOrCreateSession(player.getUniqueId());
        if (args.length < 2 || args.length == 3 || args.length > 4) {
            player.sendMessage(ChatColor.RED + "/showgen generate [action] [timestamp]");
            player.sendMessage(ChatColor.RED + "OR");
            player.sendMessage(ChatColor.RED + "/showgen generate [action] [bottom/top] [delay per layer] [timestamp]");
            return;
        }

        boolean layered = args.length >= 4;

        String action = args[0];
        double time;
        try {
            time = Double.parseDouble(args[layered ? 3 : 1]);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + args[1] + " is not a number!");
            return;
        }
        if (args[0].equalsIgnoreCase("fakeblock")) {
            if (session.getInitialScene() == null) {
                player.sendMessage(ChatColor.RED + "You must set an initial scene before running this command! Use /showgen setinitialscene");
                return;
            }
            if (session.getCorner() == null) {
                player.sendMessage(ChatColor.RED + "You must set the location of the final north-west-bottom corner to generate proper coordinates!");
                return;
            }
            GeneratorSession.ShowSelection initialScene = session.getInitialScene();
            Location corner = session.getCorner();

            LocalSession worldEditSession = WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(player));

            try {
                Region selection = worldEditSession.getSelection(worldEditSession.getSelectionWorld());

                if (selection == null) {
                    player.sendMessage(ChatColor.RED + "You need to select the blocks for the second scene with your WorldEdit wand before running this command!");
                    return;
                }

                World world = BukkitAdapter.asBukkitWorld(selection.getWorld()).getWorld();
                BlockVector3 maxVec = selection.getMaximumPoint();
                BlockVector3 minVec = selection.getMinimumPoint();
                Location finalMax = new Location(world, maxVec.getX(), maxVec.getY(), maxVec.getZ());
                Location finalMin = new Location(world, minVec.getX(), minVec.getY(), minVec.getZ());

                GeneratorSession.ShowSelection finalScene = new GeneratorSession.ShowSelection(finalMax, finalMin);

                if (!finalScene.equalSize(initialScene)) {
                    player.sendMessage(ChatColor.RED + "Your initial scene is not the same size as your current scene! They must be equal in order to generate the differences in their blocks.");
                    return;
                }

                boolean bottom;
                double delayPerLayer;

                if (layered) {
                    bottom = !args[1].equalsIgnoreCase("top");
                    delayPerLayer = Double.parseDouble(args[2]);
                } else {
                    bottom = false;
                    delayPerLayer = 0;
                }

                player.sendMessage(ChatColor.GREEN + "Generating a list of " + args[0].toLowerCase() + " changes at time " + time + " between scene 1 at " + initialScene.toString() + " and scene 2 at " + finalScene.toString());
                Bukkit.getScheduler().runTaskAsynchronously(ShowPlugin.getInstance(), () -> {
                    List<FakeBlockAction> actions = new ArrayList<>();

                    double localTime = time;

                    int startingY;
                    int endingY;
                    int yChange;
                    double changeInTime;

                    if (!layered) {
                        startingY = 0;
                        endingY = initialScene.getYLength();
                        yChange = 1;
                        changeInTime = 0;
                    } else {
                        startingY = bottom ? 0 : initialScene.getYLength();
                        endingY = bottom ? initialScene.getYLength() : 0;
                        yChange = bottom ? 1 : -1;
                        changeInTime = delayPerLayer;
                    }

                    for (int y = startingY; compare(y, endingY, !bottom); y += yChange) {
                        for (int x = 0; x < initialScene.getXLength(); x++) {
                            for (int z = 0; z < initialScene.getZLength(); z++) {
                                Block oldBlock = initialScene.getBlock(x, y, z);
                                Block newBlock = finalScene.getBlock(x, y, z);
                                if (newBlock.getType().equals(oldBlock.getType()) && newBlock.getData() == oldBlock.getData()) {
                                    continue;
                                }
                                Material material = newBlock.getType();
                                FakeBlockAction act = new FakeBlockAction(null, (long) (localTime * 1000));
                                act.setData(newBlock.getBlockData());
                                act.setLoc(new Location(corner.getWorld(), corner.getBlockX() + x, corner.getBlockY() + y, corner.getBlockZ() + z));
                                actions.add(act);
                            }
                        }
                        localTime += changeInTime;
                    }

                    if (actions.isEmpty()) {
                        player.sendMessage(ChatColor.RED + "There aren't any differences between the two selected regions!");
                        return;
                    }

                    String date = new SimpleDateFormat("MM_dd_yyyy_HH:mm:ss").format(new Date());

                    String url;
                    try {
                        url = ShowPlugin.getShowGenerator().postGist(actions, finalMax.getWorld().getName() + "_" + date + "_" + player.getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                        player.sendMessage(ChatColor.RED + "There was an error creating a GitHub Gist with the list of actions!");
                        return;
                    }
                    player.sendMessage(ChatColor.GREEN + "Finished with a list of " + actions.size() + " actions! View the actions here: " + url);
                });
            } catch (Exception e) {
                player.sendMessage(ChatColor.RED + "Error! Did you select the whole region?");
                e.printStackTrace();
            }


            return;
        }
        player.sendMessage(ChatColor.RED + "The show generator doesn't currently support '" + args[0] + "' actions.");
    }

    private boolean compare(int x, int y, boolean inverted) {
        return inverted == (x >= y);
    }
}
