package network.palace.show.pathfinding;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class Point implements Cloneable {

    @Getter private double x;
    @Getter private double y;
    @Getter private double z;

    @Getter private float yaw;
    @Getter private float pitch;
    @Getter private World world;

    public boolean isBlock() {
        return (pitch == 0.0F && yaw == 0.0F && y % 1 == 0 && z % 1 == 0 && x % 1 == 0);
    }

    public Location getLocation() {
        return new Location(world, x, y, z, yaw, pitch);
    }

    public Location getLocation(World world) {
        return new Location(world, x, y, z, yaw, pitch);
    }

    public Location in(World world) {
        return getLocation(world);
    }

    public static Point of(Player player) {
        return of(player.getLocation());
    }

    public static Point of(Entity entity) {
        return of(entity.getLocation());
    }

    public static Point of(double x, double y, double z, World world) {
        return new Point(x, y, z, 0F, 0F, world);
    }

    public static Point of(Location location) {
        return new Point(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), location.getWorld());
    }

    public static Point of(Block block) {
        return of(block.getLocation());
    }

    public double distanceSquared(Point point) {
        double x = Math.pow((this.x - point.getX()), 2);
        double y = Math.pow((this.y - point.getY()), 2);
        double z = Math.pow((this.z - point.getZ()), 2);
        return x + y + z;
    }

    public double distance(Point point) {
        return Math.sqrt(distanceSquared(point));
    }

    public Point deepCopy() {
        return new Point(x, y, z, yaw, pitch, world);
    }

    public Point add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Point subtract(double x, double y, double z) {
        return add(-1 * x, -1 * y, -1 * z);
    }

    public Point multiply(double x, double y, double z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    public Point add(Point point) {
        return add(point.getX(), point.getY(), point.getZ());
    }

    public Point subtract(Point point) {
        return subtract(point.getX(), point.getY(), point.getZ());
    }

    public Point setX(double x) {
        this.x = x;
        return this;
    }

    public Point setY(double y) {
        this.y = y;
        return this;
    }

    public Point setZ(double z) {
        this.z = z;
        return this;
    }

    public Point setYaw(float yaw) {
        this.yaw = yaw;
        return this;
    }

    public Point setPitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    @Override
    public String toString() {
        return "Point [" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", pitch=" + pitch +
                ", yaw=" + yaw +
                "]";
    }
}
