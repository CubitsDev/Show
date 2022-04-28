package network.palace.show.commands.show;

import network.palace.show.ShowPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowTabComplete implements TabCompleter {

    List<String> shows;
    public ShowTabComplete() {
        shows = getShows();
    }

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
                StringUtil.copyPartialMatches(args[1], getStoppedShows(), completions);
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
     * Gets stopped shows, not from disk.
     * @return Stopped show names (minus .show)
     */
    private List<String> getStoppedShows() {
        List<String> stoppedShows = shows;
        for (String name : ShowPlugin.getShows().keySet()) stoppedShows.remove(name);
        return stoppedShows;
    }

    /**
     * Gets all shows directly from disk.
     * @return The show names (minus .show)
     */
    private List<String> getShows() {
        List<String> names = new ArrayList<>();
        listFiles("plugins/Show/shows/", names);

        // Strip ".show"
        ArrayList<String> tempNames = new ArrayList<>();
        for (String name : names) {
            tempNames.add(name.replaceAll(".show", ""));
        }
        return tempNames;
    }

    private void listFiles(String directoryName, List<String> files) {
        File root = new File(directoryName);

        // Get all files from a directory.
        File[] fList = root.listFiles();
        if(fList != null)
            for (File file : fList) {
                if (file.isFile()) {
                    files.add(file.getName());
                } else if (file.isDirectory()) {
                    listFiles(file.getAbsolutePath(), files);
                }
            }
    }

}
