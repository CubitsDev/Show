package network.palace.show.commands.show;

import network.palace.show.ShowPlugin;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowTabComplete implements TabCompleter {

    /*
    Commands:

    /show start <name>
    /show stop <name>
    /show list

    /showgen generate <type> <time>
    /showgen setinitialscene
    /showgen setcorner <x,y,z>

    /showdebug
     */

    private static final String[] baseShowCmds = { "start", "stop", "list" };

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
                StringUtil.copyPartialMatches(args[1], Arrays.asList(getStoppedShows(((Player)sender).getWorld())), completions);
                return completions;
            }
            case "stop": {
                StringUtil.copyPartialMatches(args[1], ShowPlugin.getShows().keySet(), completions);
                return completions;
            }
        }

        // Everything else without args
        return completions;
    }

    /**
     * Gets all shows that arent currently running in the world.
     * @param world What world
     * @return The show names (minus .show)
     */
    private String[] getStoppedShows(World world) {
        File f = new File("plugins/Show/shows/" + world.getName());
        String[] fileNames = f.list();
        if (fileNames == null) return new String[]{};

        ArrayList<String> names = new ArrayList<>(List.of(fileNames));

        // Strip ".show"
        ArrayList<String> tempNames = new ArrayList<>();
        for (String name : names) {
            tempNames.add(name.replaceAll(".show", ""));
        }
        names = tempNames;

        // Remove running shows
        for (String name : ShowPlugin.getShows().keySet()) names.remove(name);

        return names.toArray(new String[0]);
    }

}
