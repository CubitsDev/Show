package network.palace.show.npc.mob;

import com.comphenix.protocol.wrappers.Vector3F;
import lombok.Getter;
import lombok.Setter;
import network.palace.show.npc.AbstractGearMob;
import network.palace.show.npc.ProtocolLibSerializers;
import network.palace.show.pathfinding.Point;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Set;

public class MobArmorStand extends AbstractGearMob {

    @Getter @Setter private boolean isSmall = false;
    @Getter @Setter private boolean arms = true;
    @Getter @Setter private boolean basePlate = true;
    @Getter @Setter private boolean isMarker = false;
    @Getter @Setter private Vector3F headPose, bodyPost, leftArmPose, rightArmPose, leftLegPose, rightLegPose;

    public MobArmorStand(Point location, Set<Player> observers, String title) {
        super(location, observers, title);
    }

    @Override
    protected void onDataWatcherUpdate() {
        int metadataIndex = 11;
        byte value = 0;
        if (isSmall) value |= 0x01;
        if (arms) value |= 0x04;
        if (!basePlate) value |= 0x08;
        if (isMarker) value |= 0x10;
        getDataWatcher().setObject(ProtocolLibSerializers.getByte(metadataIndex), value);
        updatePose(12, headPose);
        updatePose(13, bodyPost);
        updatePose(14, leftArmPose);
        updatePose(15, rightArmPose);
        updatePose(16, leftLegPose);
        updatePose(17, rightLegPose);
        super.onDataWatcherUpdate();
    }

    public void updatePose(int index, Vector3F pose) {
        if (pose != null) {
            getDataWatcher().setObject(ProtocolLibSerializers.getVector3F(index), radToDegress(pose));
        }
    }

    public Vector3F radToDegress(Vector3F angle) {
        return new Vector3F((float) (angle.getX() * 180 / Math.PI), (float) (angle.getY() * 180 / Math.PI), (float) (angle.getZ() * 180 / Math.PI));
    }

    @Override
    protected EntityType getEntityType() {
        return EntityType.ARMOR_STAND;
    }

    @Override
    public float getMaximumHealth() {
        return 2;
    }
}
