package network.palace.show.npc;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import lombok.Setter;
import network.palace.show.ShowPlugin;
import network.palace.show.npc.status.Status;
import network.palace.show.packets.AbstractPacket;
import network.palace.show.packets.server.entity.*;
import network.palace.show.pathfinding.Point;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.ref.WeakReference;
import java.util.*;

public abstract class AbstractEntity implements Observable<NPCObserver> {
    @Getter protected Point location;
    protected final Set<Player> visibleTo;
    protected final Set<NPCObserver> observers;
    protected final Set<Player> viewers;
    @Getter protected final WrappedDataWatcher dataWatcher;
    protected WrappedDataWatcher lastDataWatcher;
    //    private List<WrappedWatchableObject> lastDataWatcherObjects;
    @Getter protected boolean spawned;
    @Getter protected final int entityId;
    @Getter private int headYaw;
    protected InteractWatcher listener;

    private int[] passengers = new int[0];
    @Getter @Setter private boolean onFire, crouched, sprinting, visible = true, customNameVisible, gravity = true;

    protected ConditionalName conditionalName;
    @Setter protected String customName;

    @Getter @Setter protected UUID uuid;

    public AbstractEntity(Point location, Set<Player> observers, String title) {
        this.location = location.deepCopy();
        this.visibleTo = new HashSet<>();
        if (observers != null) this.visibleTo.addAll(observers);
        this.dataWatcher = new WrappedDataWatcher();
        this.observers = new HashSet<>();
        this.viewers = new HashSet<>();
        this.spawned = false;
        if (title == null) {
            this.customName = "";
        } else {
            this.customName = title;
        }
        this.entityId = ShowPlugin.getSoftNPCManager().getIDManager().getNextID();
    }

    {
        ShowPlugin.getSoftNPCManager().getEntityRefs().add(new WeakReference<>(this));
    }

    protected void onUpdate() {
    }

    protected void onDataWatcherUpdate() {
    }

    protected abstract EntityType getEntityType();

    private InteractWatcher createNewInteractWatcher() {
        return listener = new InteractWatcher(this);
    }

    @Override
    public final void registerObservable(NPCObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public final void unregisterObservable(NPCObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public final ImmutableSet<NPCObserver> getObservers() {
        return ImmutableSet.copyOf(observers);
    }

    public final void addVisibleTo(Player player) {
        this.visibleTo.add(player);
        if (this.isSpawned()) forceSpawn(player);
    }

    public final void removeVisibleTo(Player player) {
        this.visibleTo.remove(player);
        if (this.isSpawned()) forceDespawn(player);
    }

    protected void addViewer(Player player) {
        this.viewers.add(player);
    }

    protected void removeViewer(Player player) {
        this.viewers.remove(player);
    }

    protected boolean isViewer(Player player) {
        return this.viewers.contains(player);
    }

    public final void makeGlobal() {
        this.visibleTo.clear();
    }

    protected final Player[] getTargets() {
        Player[] cPlayers = (this.visibleTo.size() == 0 ? Bukkit.getOnlinePlayers() : this.visibleTo).toArray(new Player[this.visibleTo.size()]);
        Player[] players = new Player[cPlayers.length];
        int x = 0;

        for (Player player : cPlayers) {
            UUID uid = player.getLocation().getWorld().getUID();
            UUID uid1 = this.location.getWorld() != null ? location.getWorld().getUID() : null;

            if (this.location.getWorld() != null && !uid.equals(uid1)) continue;
            players[x] = player;
            x++;
        }
        return x == 0 ? new Player[]{} : Arrays.copyOfRange(players, 0, x);
    }

    public void spawn() {
        if (spawned) return;
        ProtocolLibrary.getProtocolManager().addPacketListener(createNewInteractWatcher());
        spawned = true;
        Player[] targets = getTargets();
        Arrays.asList(targets).forEach(t -> this.forceSpawn(t, false));
        update(targets, true);
    }

    private WrapperPlayServerEntityDestroy getDespawnPacket() {
        WrapperPlayServerEntityDestroy packet = new WrapperPlayServerEntityDestroy();
        packet.setEntityIds(new int[]{entityId});
        return packet;
    }

    public void despawn() {
        if (!spawned) return;
        ProtocolLibrary.getProtocolManager().removePacketListener(listener);
        listener = null;
        spawned = false;
        Arrays.asList(getTargets()).forEach(this::forceDespawn);
    }

    public final void forceDespawn(Player player) {
        if (!isViewer(player)) {
            return;
        }
        getDespawnPacket().sendPacket(player);
        removeViewer(player);
    }

    public final void forceSpawn(Player player) {
        forceSpawn(player, true);
    }

    public void forceSpawn(Player player, boolean update) {
        if (!isViewer(player)) {
            getSpawnPacket().sendPacket(player);
            addViewer(player);
        }

        if (update) update(new Player[]{player}, true);
    }

    protected abstract AbstractPacket getSpawnPacket();

    private WrapperPlayServerEntityStatus getStatusPacket(int status) {
        WrapperPlayServerEntityStatus packet = new WrapperPlayServerEntityStatus();
        packet.setEntityID(entityId);
        packet.setEntityStatus(status);
        return packet;
    }

    protected final void playStatus(Set<Player> players, int status) {
        players.forEach(getStatusPacket(status)::sendPacket);
    }

    protected final void playStatus(int status) {
        Arrays.asList(getTargets()).forEach(getStatusPacket(status)::sendPacket);
    }

    public final void playHurtAnimation() {
        playStatus(Status.ENTITY_HURT);
    }

    public final void playDeadAnimation() {
        playStatus(Status.ENTITY_DEAD);
    }

    public final void playHurtAnimation(Set<Player> players) {
        playStatus(players, Status.ENTITY_HURT);
    }

    public final void playDeadAnimation(Set<Player> players) {
        playStatus(players, Status.ENTITY_DEAD);
    }

    public final void update() {
        update(false);
    }

    public final void update(boolean spawn) {
        update(getTargets(), spawn);
    }

    public final void update(Player[] targets, boolean spawn) {
        try {
            if (!spawned) spawn();
            updateDataWatcher();
            List<WrappedWatchableObject> watchableObjects = new ArrayList<>();

            if (spawn) {
                watchableObjects.addAll(dataWatcher.getWatchableObjects());
            } else {
                if (lastDataWatcher == null) {
                    watchableObjects.addAll(dataWatcher.getWatchableObjects());
                } else {
                    for (WrappedWatchableObject watchableObject : dataWatcher.getWatchableObjects()) {
                        Object object = lastDataWatcher.getObject(watchableObject.getIndex());
                        if (object == null || !object.equals(watchableObject.getValue())) {
                            watchableObjects.add(watchableObject);
                        }
                    }
                }
            }

            if (hasConditionalName()) {
                for (Player target : targets) {
                    WrapperPlayServerEntityMetadata localPacket = new WrapperPlayServerEntityMetadata();
                    for (WrappedWatchableObject watchableObject : watchableObjects) {
                        if (watchableObject.getIndex() == 2) {
                            watchableObject.setValue(conditionalName.getCustomName(target));
                        }
                    }
                    localPacket.setMetadata(watchableObjects);
                    localPacket.setEntityID(entityId);
                    localPacket.sendPacket(target);
                }
            } else {
                WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata();
                packet.setMetadata(watchableObjects);
                packet.setEntityID(entityId);
                Arrays.asList(targets).forEach(packet::sendPacket);
                lastDataWatcher = deepClone(dataWatcher);
            }

            if (passengers.length > 0) {
                WrapperPlayServerMount packet = new WrapperPlayServerMount();
                packet.setEntityID(entityId);
                packet.setPassengerIds(passengers);
                for (Player target : targets) {
                    packet.sendPacket(target);
                }
            }

            onUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private WrappedDataWatcher deepClone(WrappedDataWatcher dataWatcher) {
        WrappedDataWatcher clone = new WrappedDataWatcher();
        if (MinecraftReflection.watcherObjectExists()) {
            dataWatcher.getWatchableObjects().forEach(object -> clone.setObject(object.getWatcherObject(), object));
        } else {
            dataWatcher.getWatchableObjects().forEach(object -> clone.setObject(object.getIndex(), object));
        }
        return clone;
    }

    public final void move(Point point) {
        move(point, false);
    }

    public final void move(Point point, boolean relative) {
        if (!spawned) throw new IllegalStateException("You cannot teleport something that hasn't spawned yet!");
        final Point oldLocation = this.location;
        final Point newLocation = relative ? this.location.deepCopy().add(point) : point;
        AbstractPacket packet;
        if (oldLocation.distanceSquared(newLocation) <= 16) {
            WrapperPlayServerRelEntityMoveLook moveLookPacket = new WrapperPlayServerRelEntityMoveLook();
            moveLookPacket.setEntityID(entityId);
            moveLookPacket.setDx(newLocation.getX() - oldLocation.getX());
            moveLookPacket.setDy(newLocation.getY() - oldLocation.getY());
            moveLookPacket.setDz(newLocation.getZ() - oldLocation.getZ());
            moveLookPacket.setPitch(newLocation.getPitch());
            moveLookPacket.setYaw(newLocation.getYaw());
            moveLookPacket.setOnGround(true);
            packet = moveLookPacket;
        } else {
            WrapperPlayServerEntityTeleport packet1 = new WrapperPlayServerEntityTeleport();
            packet1.setEntityID(entityId);
            packet1.setX(newLocation.getX());
            packet1.setY(newLocation.getY());
            packet1.setZ(newLocation.getZ());
            packet1.setPitch(newLocation.getPitch());
            packet1.setYaw(newLocation.getYaw());
            packet = packet1;
        }
        Arrays.asList(getTargets()).forEach(packet::sendPacket);
        this.location = newLocation;
    }

    public final void addVelocity(Vector vector) {
        WrapperPlayServerEntityVelocity packet = new WrapperPlayServerEntityVelocity();
        packet.setEntityID(entityId);
        packet.setVelocityX(vector.getX());
        packet.setVelocityY(vector.getY());
        packet.setVelocityZ(vector.getZ());
        Arrays.asList(getTargets()).forEach(packet::sendPacket);
    }

    protected final void updateDataWatcher() {
        int metadataIndex = 0;
        int customNameIndex = 2;
        int showNameTagIndex = 3;
        int noGravityIndex = 5;
        if (customNameVisible) {
            dataWatcher.setObject(ProtocolLibSerializers.getBoolean(showNameTagIndex), true);
        } else if (dataWatcher.getObject(showNameTagIndex) != null) {
            dataWatcher.remove(showNameTagIndex);
        }
        if (customName != null) {
            dataWatcher.setObject(ProtocolLibSerializers.getString(customNameIndex), customName.substring(0, Math.min(customName.length(), 64)));
        } else if (dataWatcher.getObject(customNameIndex) != null) {
            dataWatcher.remove(customNameIndex);
        }
        if (!gravity) {
            dataWatcher.setObject(ProtocolLibSerializers.getBoolean(noGravityIndex), true);
        } else if (dataWatcher.getObject(noGravityIndex) != null) {
            dataWatcher.remove(noGravityIndex);
        }
        byte zeroByte = 0;
        if (onFire) zeroByte |= 0x01;
        if (crouched) zeroByte |= 0x02;
        if (sprinting) zeroByte |= 0x08;
        if (!visible) zeroByte |= 0x20;
        dataWatcher.setObject(ProtocolLibSerializers.getByte(metadataIndex), zeroByte);
        onDataWatcherUpdate();
    }

    public final void setHeadYaw(Location location) {
        if (!spawned)
            throw new IllegalStateException("You cannot modify the rotation of the head of a non-spawned entity!");
        headYaw = (int) location.getYaw();
        sendYaw(getTargets());
    }

    public final void sendYaw(Player[] players) {
        WrapperPlayServerEntityHeadRotation packet = new WrapperPlayServerEntityHeadRotation();
        packet.setEntityID(entityId);
        packet.setHeadYaw(headYaw);
        Arrays.asList(players).forEach(packet::sendPacket);
        Entity e;
    }

    public final ImmutableSet<Player> getVisibleTo() {
        return ImmutableSet.copyOf(visibleTo);
    }

    public boolean canSee(Player player) {
        if (visibleTo.isEmpty()) {
            return true;
        }
        return visibleTo.contains(player);
    }

    public boolean sameWorld(Player player) {
        return location.getWorld().equals(player.getWorld());
    }

    public boolean hasPassengers() {
        return passengers.length > 0;
    }

    public int getPassengerCount() {
        return passengers.length;
    }

    public void addPassenger(AbstractEntity entity) {
        int[] newArray = new int[passengers.length + 1];
        int i = 0;
        for (int p : passengers) {
            newArray[i++] = p;
        }
        newArray[i] = entity.getEntityId();
        passengers = newArray;
        update(false);
    }

    public void removePassenger(AbstractEntity entity) {
        if (passengers.length < 1) return;
        int[] newArray = new int[passengers.length - 1];
        int i = 0;
        for (int p : passengers) {
            if (p == entity.getEntityId()) continue;
            if (i >= newArray.length) return;
            newArray[i++] = p;
        }
        passengers = newArray;
        update(false);
    }

    public String getCustomName() {
        return getCustomName(null);
    }

    public String getCustomName(Player player) {
        if (player == null || this.conditionalName == null || conditionalName.getCustomName(player) == null) {
            return customName;
        } else {
            return conditionalName.getCustomName(player);
        }
    }

    public void setConditionalName(ConditionalName conditionalName) {
        this.conditionalName = conditionalName;
    }

    public boolean hasConditionalName() {
        return this.conditionalName != null;
    }

    protected static class InteractWatcher extends PacketAdapter {

        private final AbstractEntity watchingFor;

        public InteractWatcher(AbstractEntity watchingFor) {
            super(ShowPlugin.getInstance(), PacketType.Play.Client.USE_ENTITY);
            this.watchingFor = watchingFor;
        }

        @Override
        public void onPacketReceiving(PacketEvent event) {
            WrapperPlayClientUseEntity packet = new WrapperPlayClientUseEntity(event.getPacket());
            if (packet.getTargetID() != watchingFor.getEntityId()) return;
            Player onlinePlayer = event.getPlayer();
            ClickAction clickAction = ClickAction.from(packet.getType().name());
            if (clickAction != null) {
                for (NPCObserver npcObserver : watchingFor.getObservers()) {
                    try {
                        npcObserver.onPlayerInteract(onlinePlayer, watchingFor, clickAction);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            event.setCancelled(true);
        }
    }
}
