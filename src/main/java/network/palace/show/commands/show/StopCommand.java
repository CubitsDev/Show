package network.palace.show.commands.show;

import network.palace.show.Show;
import network.palace.show.ShowPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * @author Marc
 * @since 8/2/17
 */
public class StopCommand {

    public void handle(CommandSender sender, String filename)  {
        if (!ShowPlugin.getShows().containsKey(filename)) {
            sender.sendMessage(ChatColor.RED + "----------------------------------------------");
            sender.sendMessage(ChatColor.GOLD + filename + ChatColor.AQUA + " is not running!");
            sender.sendMessage(ChatColor.RED + "----------------------------------------------");
        } else {
            sender.sendMessage(ChatColor.GOLD + filename + ChatColor.AQUA + " has been stopped!");
            Show s = ShowPlugin.getShows().get(filename);
            if (s == null) {
                sender.sendMessage(ChatColor.RED + "Couldn't find a show with that name!");
                return;
            }
            s.stop();
            ShowPlugin.stopShow(filename);
            ShowPlugin.getShows().remove(filename);
        }
    }
}
