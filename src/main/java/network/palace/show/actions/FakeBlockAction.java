package network.palace.show.actions;

import lombok.Getter;
import lombok.Setter;
import network.palace.show.Show;
import network.palace.show.exceptions.ShowParseException;
import network.palace.show.utils.ShowUtil;
import network.palace.show.utils.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

/**
 * Created by Marc on 7/1/15
 */
@Getter
@Setter
@SuppressWarnings("deprecation")
public class FakeBlockAction extends ShowAction {
    private Location loc;
    private BlockData data;

    public FakeBlockAction(Show show, long time) {
        super(show, time);
    }

    public FakeBlockAction(Show show, long time, Location loc, BlockData data) {
        super(show, time);
        this.loc = loc;
        this.data = data;
    }

    @Override
    public void play(Player[] nearPlayers) {
        try {
            for (Player tp : nearPlayers) {
                if (tp != null) tp.sendBlockChange(loc, data);
            }
        } catch (Exception e) {
            Bukkit.getLogger().severe("FakeBlockAction -" + ChatColor.RED + "Error sending FakeBlockAction for type (" +
                    data + ") at location " + loc.getX() + "," + loc.getY() + "," + loc.getZ() + " at time " +
                    time + " for show " + show.getName());
            e.printStackTrace();
        }
    }

    @Override
    public ShowAction load(String line, String... args) throws ShowParseException {
        Location loc = WorldUtil.strToLoc(show.getWorld().getName() + "," + args[3]);
        if (loc == null) {
            throw new ShowParseException("Invalid Location " + line);
        }
        try {
            this.loc = loc;
            this.data = ShowUtil.getBlockData(line);
        } catch (IllegalArgumentException e) {
            throw new ShowParseException(e.getMessage());
        }
        return this;
    }

    @Override
    protected ShowAction copy(Show show, long time) throws ShowParseException {
        return new FakeBlockAction(show, time, loc, data);
    }
}