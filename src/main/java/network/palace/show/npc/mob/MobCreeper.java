package network.palace.show.npc.mob;

import lombok.Setter;
import network.palace.show.npc.AbstractMob;
import network.palace.show.npc.ProtocolLibSerializers;
import network.palace.show.pathfinding.Point;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * @author Innectic
 * @since 6/25/2017
 */
public class MobCreeper extends AbstractMob {

    @Setter private boolean isCharged = false;
    @Setter private boolean isIgnited = false;

    public MobCreeper(Point location, Set<Player> observers, String title) {
        super(location, observers, title);
    }

    @Override
    protected void onDataWatcherUpdate() {
        int chargedIndex = 13;
        getDataWatcher().setObject(ProtocolLibSerializers.getBoolean(chargedIndex), isCharged);
        int ignitedIndex = 14;
        getDataWatcher().setObject(ProtocolLibSerializers.getBoolean(ignitedIndex), isIgnited);
        super.onDataWatcherUpdate();
    }

    @Override
    protected EntityType getEntityType() {
        return EntityType.CREEPER;
    }

    @Override
    public float getMaximumHealth() {
        return 20f;
    }
}