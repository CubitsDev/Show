package network.palace.show.actions;

import network.palace.show.Show;
import network.palace.show.exceptions.ShowParseException;
import network.palace.show.utils.WorldUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class BlockAction extends ShowAction {
    public Location location;
    public Material blockMat;

    public BlockAction(Show show, long time) {
        super(show, time);
    }

    public BlockAction(Show show, long time, Location location, Material blockMat) {
        super(show, time);
        this.location = location;
        this.blockMat = blockMat;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void play(Player[] nearPlayers) {
        Block block = location.getBlock();
        block.setType(blockMat);
    }

    @Override
    public ShowAction load(String line, String... args) throws ShowParseException {
        Location loc = WorldUtil.strToLoc(show.getWorld().getName() + "," + args[3]);
        if (loc == null) {
            throw new ShowParseException("Invalid Location " + line);
        }
        try {
            this.blockMat = Material.valueOf(args[2].toUpperCase());
            this.location = loc;
        } catch (IllegalArgumentException e) {
            throw new ShowParseException(e.getMessage());
        }
        return this;
    }

    @Override
    protected ShowAction copy(Show show, long time) throws ShowParseException {
        return new BlockAction(show, time, location, blockMat);
    }
}
