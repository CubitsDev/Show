package network.palace.show.actions.armor;

import network.palace.show.Show;
import network.palace.show.actions.ShowAction;
import network.palace.show.exceptions.ShowParseException;
import network.palace.show.handlers.armorstand.ShowStand;
import network.palace.show.utils.ShowUtil;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

/**
 * Created by Marc on 10/11/15
 */
public class ArmorStandDespawn extends ShowAction {
    private final ShowStand stand;

    public ArmorStandDespawn(Show show, long time, ShowStand stand) {
        super(show, time);
        this.stand = stand;
    }

    @Override
    public void play(Player[] nearPlayers) {
        if (!stand.isHasSpawned()) {
            ShowUtil.logDebug(show.getName(), "ArmorStand with ID " + stand.getId() + " has not spawned");
            return;
        }
        ArmorStand armor = stand.getStand();
        armor.remove();
        stand.setStand(null);
        stand.despawn();
    }

    @Override
    public ShowAction load(String line, String... args) throws ShowParseException {
        return this;
    }

    @Override
    protected ShowAction copy(Show show, long time) throws ShowParseException {
        return new ArmorStandDespawn(show, time, stand);
    }
}