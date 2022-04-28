package network.palace.show.commands;

import network.palace.show.commands.showgen.GenerateCommand;
import network.palace.show.commands.showgen.SetCornerCommand;
import network.palace.show.commands.showgen.SetInitialCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ShowgenCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sendHelpMsg(sender);
            return true;
        }

        if (!(sender instanceof Player)) {
             sender.sendMessage(ChatColor.RED + "You must be a player to run this command!");
        }

        switch (args[0]) {
            case "generate":
                String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
                new GenerateCommand().handle(sender, newArgs);
                break;
            case "setcorner":
                new SetCornerCommand().handle(sender, args);
                break;
            case "setinitialscene":
                new SetInitialCommand().handle(sender);
                break;
            default:
                sendHelpMsg(sender);
                break;
        }
        return true;
    }

    /**
     * Sends a help message to the player.
     * @param sender Who to send it to.
     */
    private void sendHelpMsg(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "ShowGen Commands:");
        sender.sendMessage(ChatColor.AQUA + "/showgen generate [action] [bottom/top] [delay per layer] [timestamp]" + ChatColor.GREEN + "- Generate blocks of show actions with one command");
        sender.sendMessage(ChatColor.AQUA + "/showgen setcorner x,y,z" + ChatColor.GREEN + "- Set the location of the final north-west-bottom corner to help give real coordinate values");
        sender.sendMessage(ChatColor.AQUA + "/showgen setinitialscene " + ChatColor.GREEN + "- Set the initial scene for a generator session");
    }
}
