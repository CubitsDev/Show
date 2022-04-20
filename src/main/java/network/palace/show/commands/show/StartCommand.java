package network.palace.show.commands.show;


import network.palace.show.Show;
import network.palace.show.ShowPlugin;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.io.File;

/**
 * @author Marc
 * @since 8/2/17
 */
public class StartCommand  {

    public void handle(CommandSender sender, String filename, World world) {
        if (world == null || world.getName() == null) {
            sender.sendMessage(ChatColor.RED + "Invalid world!");
            return;
        }
        if (filename == null | filename.equals("")) {
            sender.sendMessage(ChatColor.RED + "/show start <name>");
            return;
        }
        if (ShowPlugin.getShows().containsKey(filename)) {
            sender.sendMessage(ChatColor.RED + "----------------------------------------------");
            sender.sendMessage(ChatColor.RED + "That show is already running!");
            sender.sendMessage(ChatColor.RED + "----------------------------------------------");
            return;
        }
        File f = new File("plugins/Show/shows/" + world.getName() + "/" + filename + ".show");
        if (!f.exists()) {
            sender.sendMessage(ChatColor.RED + "----------------------------------------------");
            sender.sendMessage(ChatColor.RED + "That show doesn't exist! Looking at: " + f.getPath());
            sender.sendMessage(ChatColor.RED + "----------------------------------------------");
            return;
        }
        sender.sendMessage(ChatColor.GREEN + "Starting... ");
        ShowPlugin.startShow(filename, new Show(f, world));
        sender.sendMessage(ChatColor.GREEN + filename + ChatColor.AQUA + " has started on world " + ChatColor.YELLOW + world.getName());
    }
}
