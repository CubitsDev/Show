package network.palace.show.commands.showgen;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowGenTabComplete implements TabCompleter {

    /*
    Commands:

    /show start <name>
    /show stop <name>
    /show reload
    /show list

    /showgen generate <type> [top/bottom] <layer-delay> <timestamp>
    /showgen setinitialscene
    /showgen setcorner <x,y,z>

    /showdebug
     */

    private static final String[] baseShowGenCmds = { "generate", "setinitialscene", "setcorner" };
    private static final String[] actionTypes = { "fakeblock" };
    private static final String[] topOrBottom = { "top", "bottom" };

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> completions = new ArrayList<>();

        // Return all subcommands if nothing has been typed
        if (args.length == 0) {
            completions.addAll(Arrays.asList(baseShowGenCmds));
            return completions;
        }

        // Return applicable commands if started typing
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], Arrays.asList(baseShowGenCmds), completions);
            return completions;
        }

        // Handle subcommand args
        switch (args[0]) {
            case "generate": {

                // Params
                if (args.length == 2) {
                    completions.addAll(Arrays.asList(actionTypes));
                } else if (args.length == 3) {
                    completions.addAll(Arrays.asList(topOrBottom));
                }

                return completions;
            }
            case "setcorner": {
                Player player = (Player) sender;

                // Return players coords
                if (args.length == 2) { // X
                    completions.add(player.getLocation().getX()+",");
                } else if (args.length == 3) { // Y
                    completions.add(player.getLocation().getY()+",");
                } else if (args.length == 4) { // Z
                    completions.add(player.getLocation().getZ()+",");
                }

                return completions;
            }
        }

        // Everything else without args
        return completions;
    }

}
