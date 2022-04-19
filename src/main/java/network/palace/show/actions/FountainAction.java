package network.palace.show.actions;

import network.palace.show.Show;
import network.palace.show.ShowPlugin;
import network.palace.show.exceptions.ShowParseException;
import network.palace.show.handlers.Fountain;
import network.palace.show.utils.WorldUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class FountainAction extends ShowAction {
    private double duration;
    private Location loc;
    private Vector force;
    private Material mat;

    public FountainAction(Show show, long time) {
        super(show, time);
    }

    public FountainAction(Show show, long time, double duration, Location loc, Material mat, Vector force) {
        super(show, time);
        this.duration = duration;
        this.loc = loc;
        this.force = force;
        this.mat = mat;
    }

    @Override
    public void play(Player[] nearPlayers) {
        ShowPlugin.getInstance().getFountainManager().addFountain(new Fountain(loc, duration, mat, force));
    }

    @Override
    public ShowAction load(String line, String... args) throws ShowParseException {
        Location loc = WorldUtil.strToLoc(show.getWorld().getName() + "," + args[4]);
        Double[] values = WorldUtil.strToDoubleList(show.getWorld().getName() + "," + args[5]);
        double duration = Double.parseDouble(args[3]);
        Vector force = new Vector(values[0], values[1], values[2]);
        try {
            this.duration = duration;
            this.loc = loc;
            this.force = force;
            this.mat = Material.valueOf(args[2].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ShowParseException(e.getMessage());
        }
        return this;
    }

    @Override
    protected ShowAction copy(Show show, long time) throws ShowParseException {
        return new FountainAction(show, time, duration, loc, mat, force);
    }
}