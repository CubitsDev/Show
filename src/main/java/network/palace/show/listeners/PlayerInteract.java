package network.palace.show.listeners;

import network.palace.show.ShowPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;

/**
 * Created by Marc on 1/15/17.
 */
public class PlayerInteract implements Listener {
    public static String show = ChatColor.BLUE + "[Show]";

//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void onPlayerInteract(PlayerInteractEvent event) {
//        CPlayer player = Core.getPlayerManager().getPlayer(event.getPlayer());
//        if (player == null) return;
//        Action action = event.getAction();
//        Rank rank = player.getRank();
//        final ItemStack hand = player.getInventory().getItemInMainHand();
//        if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
//            Material type = event.getClickedBlock().getType();
//            if (type.equals(Material.SIGN) || type.equals(Material.SIGN_POST) || type.equals(Material.WALL_SIGN)) {
//                Sign s = (Sign) event.getClickedBlock().getState();
//                if (s.getLine(0).equals(show)) {
//                    String show = ChatColor.stripColor(s.getLine(3));
//                    ShowPlugin.getShows().values().stream().filter(sh -> sh.getName().equals(show)).forEach(sh -> {
//                        sh.syncAudioForPlayer(player);
//                        player.sendMessage(ChatColor.GREEN + "Syncing your audio for " + show + "!");
//                    });
//                }
//            }
//        }
//    }

    @EventHandler
    public void onManipulateArmorStand(PlayerArmorStandManipulateEvent event) {
        event.setCancelled(event.getRightClicked().getMetadata("show").stream()
                .filter(metadataValue -> metadataValue.getOwningPlugin() instanceof ShowPlugin).anyMatch(MetadataValue::asBoolean));
    }

    @EventHandler
    public void onPunchEnderCrystal(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.ENDER_CRYSTAL) {
            EnderCrystal enderCrystal = (EnderCrystal) event.getEntity();
            event.setCancelled(enderCrystal.getMetadata("show").stream().anyMatch(MetadataValue::asBoolean));
        }
    }
}
