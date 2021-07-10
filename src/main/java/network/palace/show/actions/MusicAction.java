package network.palace.show.actions;

import network.palace.show.Show;
import network.palace.show.exceptions.ShowParseException;
import org.bukkit.entity.Player;

public class MusicAction extends ShowAction {
    private int record;

    public MusicAction(Show show, long time) {
        super(show, time);
    }

    public MusicAction(Show show, long time, int record) {
        super(show, time);
        this.record = record;
    }

    @Override
    public void play(Player[] nearPlayers) {
        show.playMusic(record);
    }

    @Override
    public ShowAction load(String line, String... args) throws ShowParseException {
        try {
            this.record = Integer.parseInt(args[2]);
        } catch (Exception e) {
            throw new ShowParseException("Invalid Material");
        }
        return this;
    }

    @Override
    protected ShowAction copy(Show show, long time) throws ShowParseException {
        return new MusicAction(show, time, record);
    }
}
