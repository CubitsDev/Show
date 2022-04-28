package network.palace.show.commands;


import network.palace.show.commands.show.ListCommand;
import network.palace.show.commands.show.StartCommand;
import network.palace.show.commands.show.StopCommand;
import org.bukkit.ChatColor;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Marc on 12/6/16.
 * Updated by Tom
 */
public class ShowCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelpMsg(sender);
            return true;
        } else {
            switch (args[0]) {
                case "list":
                    new ListCommand().runList(sender);
                    break;
                case "start":
                    if (sender instanceof Player) {
                        if (args.length < 2) {
                            sender.sendMessage(ChatColor.RED + "/show start <name>");
                            return true;
                        }
                        new StartCommand().handle(sender, args[1], ((Player) sender).getWorld());
                    } else if (sender instanceof BlockCommandSender) {
                        if (args.length < 2) {
                            sender.sendMessage(ChatColor.RED + "/show start <name>");
                            return true;
                        }
                        new StartCommand().handle(sender, args[1], ((BlockCommandSender) sender).getBlock().getWorld());
                    } else {
                        sender.sendMessage(ChatColor.RED + "You cannot run this from the console!");
                    }
                    break;
                case "stop":
                    if (sender instanceof Player) {
                        if (args.length < 2) {
                            sender.sendMessage(ChatColor.RED + "/show stop <name>");
                            return true;
                        }
                        new StopCommand().handle(sender, args[1]);
                    } else if (sender instanceof CommandBlock) {
                        if (args.length < 2) {
                            sender.sendMessage(ChatColor.RED + "/show stop <name>");
                            return true;
                        }
                        new StopCommand().handle(sender, args[1]);
                    } else {
                        sender.sendMessage(ChatColor.RED + "You cannot run this from the console!");
                    }
                    break;
                default:
                    sendHelpMsg(sender);
                    break;
            }
        }

        return true;
    }

    /**
     * Sends a help message to the player.
     * @param sender Who to send it to.
     */
    private void sendHelpMsg(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "Show Commands:");
        sender.sendMessage(ChatColor.AQUA + "/show list " + ChatColor.GREEN + "- List all running shows");
        sender.sendMessage(ChatColor.AQUA + "/show start [Show Name] " + ChatColor.GREEN + "- Start a show");
        sender.sendMessage(ChatColor.AQUA + "/show stop [Show Name] " + ChatColor.GREEN + "- Stop a show");
    }
}
