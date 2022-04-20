package network.palace.show.actions.armor;

import network.palace.show.Show;
import network.palace.show.ShowPlugin;
import network.palace.show.actions.ShowAction;
import network.palace.show.exceptions.ShowParseException;
import network.palace.show.handlers.ArmorData;
import network.palace.show.handlers.armorstand.ShowStand;
import network.palace.show.utils.ShowUtil;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * Created by Marc on 10/11/15
 */
public class ArmorStandSpawn extends ShowAction {
    private final ShowStand stand;
    private final Location loc;

    public ArmorStandSpawn(Show show, long time, ShowStand stand, Location loc) {
        super(show, time);
        this.stand = stand;
        this.loc = loc;
    }

    @Override
    public void play(Player[] nearPlayers) {
        if (stand.isHasSpawned()) {
            ShowUtil.logDebug(show.getName(), "ArmorStand with ID " + stand.getId() + " has spawned already");
            return;
        }
        ArmorStand armor = loc.getWorld().spawn(loc, ArmorStand.class);
        stand.spawn();
        armor.setCustomName(stand.getId());
        armor.setArms(true);
        armor.setBasePlate(false);
        armor.setGravity(false);
        armor.setSilent(true);
        armor.setSmall(stand.isSmall());
        armor.setMetadata("show", new FixedMetadataValue(ShowPlugin.getInstance(), true));
        ArmorData data = stand.getArmorData();
        if (data != null) {
            if (data.getHead() != null) {
                armor.setHelmet(data.getHead());
            }
            if (data.getChestplate() != null) {
                armor.setChestplate(data.getChestplate());
            }
            if (data.getLeggings() != null) {
                armor.setLeggings(data.getLeggings());
            }
            if (data.getBoots() != null) {
                armor.setBoots(data.getBoots());
            }

            if (data.getItemInMainHand() != null) {
                armor.setItemInHand(data.getItemInMainHand());
            }
            if (data.getItemInOffHand() != null) {
                armor.getEquipment().setItemInOffHand(data.getItemInOffHand());
            }
        }
        stand.setStand(armor);
    }

    @Override
    public ShowAction load(String line, String... args) throws ShowParseException {
        return this;
    }

    @Override
    protected ShowAction copy(Show show, long time) throws ShowParseException {
        return new ArmorStandSpawn(show, time, stand, loc);
    }
}
