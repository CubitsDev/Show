package network.palace.show.commands;

import network.palace.show.ShowPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShowDebugCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!ShowPlugin.debugMap.containsKey(player.getDisplayName())) {
                ShowPlugin.debugMap.put(player.getDisplayName(), true);
                player.sendMessage(ChatColor.AQUA + "[ShowDebug] - " + ChatColor.GREEN + "Enabled");
            } else {
                ShowPlugin.debugMap.remove(player.getDisplayName());
                player.sendMessage(ChatColor.AQUA + "[ShowDebug] - " + ChatColor.RED + "Disabled");
            }
        }
        return false;
    }
}
