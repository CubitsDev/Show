package network.palace.show;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.utility.MinecraftVersion;
import com.craftmend.openaudiomc.api.interfaces.AudioApi;
import com.craftmend.openaudiomc.spigot.OpenAudioMcSpigot;
import lombok.Getter;
import network.palace.show.commands.*;
import network.palace.show.generator.ShowGenerator;
import network.palace.show.listeners.ChunkListener;
import network.palace.show.listeners.PlayerInteract;
import network.palace.show.npc.SoftNPCManager;
import network.palace.show.utils.BuildUtil;
import network.palace.show.utils.FileUtil;
import network.palace.show.utils.UpdateUtil;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.LoadOrder;
import org.bukkit.plugin.java.annotation.plugin.LogPrefix;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marc on 12/6/16.
 * Updated to be Core free by Tom 07/10/2021
 */
@Plugin(name = "Show", version = "1.5.7")
@Description(value = "Create Shows in Minecraft with easy to use files!")
@LoadOrder(value = PluginLoadOrder.POSTWORLD)
@Author(value = "Legobuilder0813")
@Author(value = "Parker")
@Author(value = "Innectic")
@Author(value = "Cubits")
@LogPrefix(value = "Show")
@Dependency(value = "WorldEdit")
@Dependency(value = "ProtocolLib")
@Dependency(value = "OpenAudioMc")
@Command(name = "show", desc = "Main show command", permission = "show.main", permissionMessage = "You do not have permission!", usage = "/show [list|start|stop]")
@Command(name = "showdebug", desc = "Showdebug command", permission = "show.debug", permissionMessage = "You do not have permission!", usage = "/showdebug")

public class ShowPlugin extends JavaPlugin {
    @Getter private ArmorStandManager armorStandManager;
    @Getter private FountainManager fountainManager;
    @Getter private static ShowGenerator showGenerator;
    @Getter private static BuildUtil buildUtil;
    @Getter private static SoftNPCManager softNPCManager;
    @Getter private final boolean isMinecraftGreaterOrEqualTo11_2 = MinecraftVersion.getCurrentVersion().getMinor() >= 12;
    @Getter private static AudioApi audioApi;
    @Getter private static OpenAudioMcSpigot openAudioMcSpigot;
    private static ShowPlugin instance;
    private static final HashMap<String, Show> shows = new HashMap<>();

    private int taskid = 0;

    public static ShowPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        armorStandManager = new ArmorStandManager();
        fountainManager = new FountainManager();
        showGenerator = new ShowGenerator();
        buildUtil = new BuildUtil();
        softNPCManager = new SoftNPCManager();
        audioApi = AudioApi.getInstance();
        openAudioMcSpigot = OpenAudioMcSpigot.getInstance();
        FileUtil.setupFiles();
        this.getCommand("show").setExecutor(new ShowCommand());
        this.getCommand("showdebug").setExecutor(new ShowDebugCommand());

        this.getServer().getPluginManager().registerEvents(fountainManager, this);
        this.getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        this.getServer().getPluginManager().registerEvents(new ChunkListener(), this);

        // Show Ticker
        taskid = Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Map.Entry<String, Show> entry : new HashMap<>(shows).entrySet()) {
                Show show = entry.getValue();
                if (show.update()) {
                    show.stop();
                    shows.remove(entry.getKey());
                }
            }
        }, 0L, 1L).getTaskId();

        int pluginId = 12010;

        Metrics metrics = new Metrics(this, pluginId);

        new UpdateUtil(this, 94141).getVersion(v -> {
            if (!this.getDescription().getVersion().equalsIgnoreCase(v)) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "A New update is available for Show! It is always recommended that you upgrade! Link: https://www.spigotmc.org/resources/show-make-huge-spectaculars-in-minecraft.94141/");
            }
        });

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Show is now enabled!");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Huge shoutout to Legobuilder0813 for making this work for The Palace Network. Time to let your awesome code shine");
    }

    @Override
    public void onDisable() {
        ProtocolLibrary.getProtocolManager().removePacketListeners(this);
        int size = shows.size();
        if (size > 0) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("shows.main") | p.isOp()) {
                    p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Reloading Show plugin, there are currently " +
                            size + " shows running!");
                }
            }
        }
        Bukkit.getScheduler().cancelTask(taskid);
        for (Show s : shows.values()) {
            s.stop();
        }
        shows.clear();
    }

    public static HashMap<String, Show> getShows() {
        return new HashMap<>(shows);
    }

    public static HashMap<String, Boolean> debugMap = new HashMap<String, Boolean>();

    public static void startShow(String name, Show show) {
        shows.put(name, show);
    }

    public static void stopShow(String name) {
        shows.remove(name);
    }
}
