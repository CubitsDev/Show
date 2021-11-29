package network.palace.show.generator;

import com.goebl.david.Request;
import com.goebl.david.Webb;
import com.google.gson.JsonObject;
import network.palace.show.ShowPlugin;
import network.palace.show.actions.FakeBlockAction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ShowGenerator {
    private final String ACCESS_TOKEN = ShowPlugin.getInstance().getGithubToken();
    private HashMap<UUID, GeneratorSession> generatorSessions = new HashMap<>();

    public GeneratorSession getSession(UUID uuid) {
        return generatorSessions.get(uuid);
    }

    public GeneratorSession getOrCreateSession(UUID uuid) {
        GeneratorSession session = getSession(uuid);
        if (session == null) {
            session = new GeneratorSession(uuid);
            addSession(session);
        }
        return session;
    }

    public void addSession(GeneratorSession session) {
        generatorSessions.put(session.getUuid(), session);
    }

    public void removeSession(UUID uuid) {
        generatorSessions.remove(uuid);
    }

    public String postGist(List<FakeBlockAction> actions, String name) throws Exception {
        Webb webb = Webb.create();

        JsonObject obj = new JsonObject();
        obj.addProperty("description", "Generated by Show v" + ShowPlugin.getInstance().getDescription().getVersion() + " on " + ShowPlugin.getInstance().getServerIp() + " at " + System.currentTimeMillis());
        obj.addProperty("public", "false");

        JsonObject files = new JsonObject();
        JsonObject file = new JsonObject();

        StringBuilder content = new StringBuilder();

        for (FakeBlockAction action : actions) {
            Location loc = action.getLoc();
            double time = ((int) ((action.getTime() / 1000.0) * 10)) / 10.0;
            Material mat = action.getMat();
            int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();
            String actionString = time + "\u0009" + "FakeBlock" + "\u0009" + mat.toString() + "\u0009" + x + "," + y + "," + z;
            content.append(actionString).append("\n");
        }

        file.addProperty("content", content.toString());

        files.add(name + ".show", file);

        obj.add("files", files);

        System.out.println("SENDING (" + actions.size() + "): " + obj.toString());

        Request req = webb.post("https://api.github.com/gists")
                .header("Accept", "application/vnd.github.v3+json")
                .header("Authorization", "token " + ACCESS_TOKEN)
                .header("Content-Type", "application/json")
                .body(obj);

        JSONObject response = req.asJsonObject().getBody();

        return response.getString("html_url");
    }
}
