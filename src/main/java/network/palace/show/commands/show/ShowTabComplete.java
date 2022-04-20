package network.palace.show.commands.show;

import network.palace.show.ShowPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowTabComplete implements TabCompleter {

    /*
    Commands:

    /show start <name>
    /show stop <name>
    /show reload
    /show list

    /showgen generate <type> <time>
    /showgen setinitialscene
    /showgen setcorner <x,y,z>

    /showdebug
     */

    private static final String[] baseShowCmds = { "start", "stop", "list", "reload"};

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> completions = new ArrayList<>();

        // Return all subcommands if nothing has been typed
        if (args.length == 0) {
            completions.addAll(Arrays.asList(baseShowCmds));
            return completions;
        }

        // Return applicable commands if started typing
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], Arrays.asList(baseShowCmds), completions);
            return completions;
        }

        // Handle subcommand args
        switch (args[0]) {
            case "start": {
                // TODO add all not running shows
                completions.add("Unsupported");
                return completions;
            }
            case "stop": {
                completions.addAll(ShowPlugin.getShows().keySet());
                return completions;
            }
        }

        // Everything else without args
        return completions;
    }

}
