package network.palace.show.utils;

import network.palace.show.Show;
import network.palace.show.ShowPlugin;
import network.palace.show.exceptions.ShowParseException;
import network.palace.show.handlers.ArmorData;
import network.palace.show.handlers.BlockData;
import network.palace.show.handlers.TitleType;
import network.palace.show.sequence.ShowSequence;
import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Marc
 * @since 8/2/17
 */
public class ShowUtil {

    public static BlockData getBlockData(String s) throws ShowParseException {
        String[] list;
        if (s.contains(":")) {
            list = s.split(":");
        } else {
            list = null;
        }
        try {
            int id;
            byte data;
            if (list != null) {
                id = Integer.parseInt(list[0]);
                data = Byte.parseByte(list[1]);
            } else {
                id = Integer.parseInt(s);
                data = (byte) 0;
            }
            return new BlockData(id, data);
        } catch (Exception ignored) {
            throw new ShowParseException("Invalid Block ID or Block data");
        }
    }

    @SuppressWarnings("deprecation")
    public static Material convertMaterial(int ID, byte Data) {
        for(Material i : EnumSet.allOf(Material.class)) if(i.getId() == ID) return Bukkit.getUnsafe().fromLegacy(new MaterialData(i, Data));
        return null;
    }

    @SuppressWarnings("deprecation")
    public static Material convertMaterialNoData(int ID) {
        for(Material i : EnumSet.allOf(Material.class)) if(i.getId() == ID) return Bukkit.getUnsafe().fromLegacy(new MaterialData(i));
        return null;
    }

    public static int getInt(String s) throws ShowParseException {
        if (!MiscUtil.checkIfInt(s)) {
            throw new ShowParseException("This isn't a number: " + s);
        }
        return Integer.parseInt(s);
    }

    public static TitleType getTitleType(String s) {
        if (s.equalsIgnoreCase("subtitle")) {
            return TitleType.SUBTITLE;
        }
        return TitleType.TITLE;
    }

    public static void runSequences(LinkedList<ShowSequence> set, long startTime) {
        if (set == null) return;
        List<ShowSequence> sequences = new ArrayList<>(set);
        for (ShowSequence sequence : sequences) {
            if (sequence == null) continue;
            try {
                if (System.currentTimeMillis() - startTime < sequence.getTime()) {
                    continue;
                }
                if (sequence.run()) set.remove(sequence);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Color colorFromString(String s) throws ShowParseException {
        switch (s.toLowerCase()) {
            case "red":
                return Color.fromRGB(170, 0, 0);
            case "orange":
                return Color.fromRGB(255, 102, 0);
            case "yellow":
                return Color.fromRGB(255, 222, 0);
            case "green":
                return Color.fromRGB(0, 153, 0);
            case "aqua":
                return Color.fromRGB(0, 255, 255);
            case "blue":
                return Color.fromRGB(51, 51, 255);
            case "purple":
                return Color.fromRGB(39, 31, 155);
            case "pink":
                return Color.fromRGB(255, 0, 255);
            case "white":
                return Color.fromRGB(255, 255, 255);
            case "black":
                return Color.fromRGB(0, 0, 0);
        }
        throw new ShowParseException("Unknown color " + s);
    }

    public static FireworkEffect parseEffect(String effect) throws ShowParseException {
        String[] tokens = effect.split(",");

        // Shape
        FireworkEffect.Type shape;
        try {
            shape = FireworkEffect.Type.valueOf(tokens[0]);
        } catch (Exception e) {
            throw new ShowParseException("Invalid type [" + tokens[0] + "] for effect " + effect);
        }

        // Color
        List<Color> colors = new ArrayList<>();
        for (String color : tokens[1].split("&")) {
            if (color.equalsIgnoreCase("AQUA")) {
                colors.add(Color.AQUA);
            } else if (color.equalsIgnoreCase("BLACK")) {
                colors.add(Color.BLACK);
            } else if (color.equalsIgnoreCase("BLUE")) {
                colors.add(Color.BLUE);
            } else if (color.equalsIgnoreCase("FUCHSIA")) {
                colors.add(Color.FUCHSIA);
            } else if (color.equalsIgnoreCase("GRAY")) {
                colors.add(Color.GRAY);
            } else if (color.equalsIgnoreCase("GREEN")) {
                colors.add(Color.GREEN);
            } else if (color.equalsIgnoreCase("LIME")) {
                colors.add(Color.LIME);
            } else if (color.equalsIgnoreCase("MAROON")) {
                colors.add(Color.MAROON);
            } else if (color.equalsIgnoreCase("NAVY")) {
                colors.add(Color.NAVY);
            } else if (color.equalsIgnoreCase("OLIVE")) {
                colors.add(Color.OLIVE);
            } else if (color.equalsIgnoreCase("ORANGE")) {
                colors.add(Color.ORANGE);
            } else if (color.equalsIgnoreCase("PURPLE")) {
                colors.add(Color.PURPLE);
            } else if (color.equalsIgnoreCase("RED")) {
                colors.add(Color.RED);
            } else if (color.equalsIgnoreCase("SILVER")) {
                colors.add(Color.SILVER);
            } else if (color.equalsIgnoreCase("TEAL")) {
                colors.add(Color.TEAL);
            } else if (color.equalsIgnoreCase("WHITE")) {
                colors.add(Color.WHITE);
            } else if (color.equalsIgnoreCase("YELLOW")) {
                colors.add(Color.YELLOW);
            } else if (color.contains(";")) {
                String[] list = color.split(";");
                colors.add(Color.fromRGB(getInt(list[0]), getInt(list[1]), getInt(list[2])));
            } else {
                throw new ShowParseException("Invalid color [" + tokens[0] + "] for effect " + effect);
            }
        }
        if (colors.isEmpty()) {
            throw new ShowParseException("No valid colors " + effect);
        }
        // Fade
        List<Color> fades = new ArrayList<>();
        if (tokens.length > 2) {
            for (String color : tokens[2].split("&")) {
                if (color.equalsIgnoreCase("AQUA")) {
                    fades.add(Color.AQUA);
                } else if (color.equalsIgnoreCase("BLACK")) {
                    fades.add(Color.BLACK);
                } else if (color.equalsIgnoreCase("BLUE")) {
                    fades.add(Color.BLUE);
                } else if (color.equalsIgnoreCase("FUCHSIA")) {
                    fades.add(Color.FUCHSIA);
                } else if (color.equalsIgnoreCase("GRAY")) {
                    fades.add(Color.GRAY);
                } else if (color.equalsIgnoreCase("GREEN")) {
                    fades.add(Color.GREEN);
                } else if (color.equalsIgnoreCase("LIME")) {
                    fades.add(Color.LIME);
                } else if (color.equalsIgnoreCase("MAROON")) {
                    fades.add(Color.MAROON);
                } else if (color.equalsIgnoreCase("NAVY")) {
                    fades.add(Color.NAVY);
                } else if (color.equalsIgnoreCase("OLIVE")) {
                    fades.add(Color.OLIVE);
                } else if (color.equalsIgnoreCase("ORANGE")) {
                    fades.add(Color.ORANGE);
                } else if (color.equalsIgnoreCase("PURPLE")) {
                    fades.add(Color.PURPLE);
                } else if (color.equalsIgnoreCase("RED")) {
                    fades.add(Color.RED);
                } else if (color.equalsIgnoreCase("SILVER")) {
                    fades.add(Color.SILVER);
                } else if (color.equalsIgnoreCase("TEAL")) {
                    fades.add(Color.TEAL);
                } else if (color.equalsIgnoreCase("WHITE")) {
                    fades.add(Color.WHITE);
                } else if (color.equalsIgnoreCase("YELLOW")) {
                    fades.add(Color.YELLOW);
                } else if (color.contains(";")) {
                    String[] list = color.split(";");
                    colors.add(Color.fromRGB(getInt(list[0]), getInt(list[1]), getInt(list[2])));
                } else if (color.equalsIgnoreCase("FLICKER") || color.equalsIgnoreCase("TRAIL")) {
                    break;
                } else {
                    throw new ShowParseException("Invalid fade color [" + color + "] for effect " + effect);
                }
            }
        }
        boolean flicker = effect.toLowerCase().contains("flicker");
        boolean trail = effect.toLowerCase().contains("trail");
        // Firework
        return FireworkEffect.builder().with(shape).withColor(colors).withFade(fades).flicker(flicker).trail(trail).build();
    }

    public static Particle getParticle(String s) {
        switch (s.toLowerCase()) {
            case "barrier":
                return Particle.BARRIER;
            case "bubble":
                return Particle.WATER_BUBBLE;
            case "cloud":
                return Particle.CLOUD;
            case "crit":
                return Particle.CRIT;
            case "depthsuspend":
                return Particle.SUSPENDED_DEPTH;
            case "dragonbreath":
                return Particle.DRAGON_BREATH;
            case "driplava":
                return Particle.DRIP_LAVA;
            case "dripwater":
                return Particle.DRIP_WATER;
            case "enchantmenttable":
                return Particle.ENCHANTMENT_TABLE;
            case "explode":
                return Particle.EXPLOSION_NORMAL;
            case "fireworksspark":
                return Particle.FIREWORKS_SPARK;
            case "flame":
                return Particle.FLAME;
            case "happyvillager":
                return Particle.VILLAGER_HAPPY;
            case "heart":
                return Particle.HEART;
            case "hugeexplosion":
                return Particle.EXPLOSION_HUGE;
            case "instantspell":
                return Particle.SPELL_INSTANT;
            case "largeexplode":
                return Particle.EXPLOSION_LARGE;
            case "largesmoke":
                return Particle.SMOKE_LARGE;
            case "lava":
                return Particle.LAVA;
            case "magiccrit":
                return Particle.CRIT_MAGIC;
            case "mobspell":
                return Particle.SPELL_MOB;
            case "mobspellambient":
                return Particle.SPELL_MOB_AMBIENT;
            case "note":
                return Particle.NOTE;
            case "portal":
                return Particle.PORTAL;
            case "reddust":
                return Particle.REDSTONE;
            case "slime":
                return Particle.SLIME;
            case "smoke":
                return Particle.SMOKE_NORMAL;
            case "snowballpoof":
                return Particle.SNOWBALL;
            case "snowshovel":
                return Particle.SNOW_SHOVEL;
            case "spell":
                return Particle.SPELL;
            case "spit":
                return Particle.SPIT;
            case "splash":
                return Particle.WATER_SPLASH;
            case "suspend":
                return Particle.SUSPENDED;
            case "totem":
                return Particle.TOTEM;
            case "townaura":
                return Particle.TOWN_AURA;
            case "wake":
                return Particle.WATER_WAKE;
            case "witchmagic":
                return Particle.SPELL_WITCH;
        }
        return Particle.valueOf(s);
    }

    public static PotionEffect getInvisibility() {
        return new PotionEffect(PotionEffectType.INVISIBILITY, 200000, 0, true);
    }

    public static boolean areLocationsEqual(Location loc1, Location loc2, int decimalPlace) {
        return loc1.getWorld().equals(loc2.getWorld()) && loc1.distance(loc2) <= (decimalPlace * 0.1);
    }

    private static double format(DecimalFormat format, double num) {
        return Double.parseDouble(format.format(num));
    }

    public static void logDebug(String showName, String message) {
        Bukkit.getLogger().info("ShowDebug - " + showName + " " + message);
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.hasPermission("show.debug") && ShowPlugin.debugMap.containsKey(p.getDisplayName()))
                .forEach(p -> p.sendMessage(ChatColor.AQUA + "[ShowDebug - " + showName + "] " + ChatColor.YELLOW + message));

        Show s = ShowPlugin.getInstance().getShows().get(showName);
        if (s != null) s.debug();
    }

    /*
    Input:
    LEATHER_HELMET;LEATHER_CHESTPLATE;LEATHER_LEGGINGS;LEATHER_BOOTS;WOOD_SWORD;TOTEM_OF_UNDYING
    skull:<playerTextureResourceHash>;LEATHER_CHESTPLATE;LEATHER_LEGGINGS;LEATHER_BOOTS;WOOD_SWORD;TOTEM_OF_UNDYING
    LEATHER_HELMET;LEATHER_CHESTPLATE:(5,5,5);LEATHER_LEGGINGS;LEATHER_BOOTS;WOOD_SWORD;TOTEM_OF_UNDYING
     */

    /**
     * Parses the armor data from the string.
     * @param s Input string. Format: head;chest;leg;boot;hand;offhand
     * @return The armor data
     * @throws Exception If there was an error parsing data
     */
    public static ArmorData parseArmorData(String s) throws Exception {
        String[] list = s.split(";");
        ItemStack head = new ItemStack(Material.AIR);
        ItemStack chestplate = new ItemStack(Material.AIR);
        ItemStack leggings = new ItemStack(Material.AIR);
        ItemStack boots = new ItemStack(Material.AIR);
        ItemStack itemInMainHand = new ItemStack(Material.AIR);
        ItemStack itemInOffHand = new ItemStack(Material.AIR);

        // Get itemstack from string
        int slot = -1;
        for (String data : list) {
            slot++;

            switch (slot) {

                case 0: { // Head

                    // Try to parse skull
                    if (data.startsWith("skull")) {
                        head = HeadUtil.getPlayerHead(data.split(":")[1]);
                        continue;
                    }

                    // Try to color leather armor
                    if (data.endsWith(")")) {
                        // Returns colored leather, or whatever item was passed in if not leather
                        head = parseColoredArmor(data);
                        continue;
                    }

                    if (MiscUtil.checkIfInt(data)) throw new ShowParseException("Legacy (1.12) numeric IDs are no longer supported.  Please convert to modern names.");
                    head = new ItemStack(Material.valueOf(data)); // Get material
                }
                case 1: { // Chestplate

                    // Try to color leather armor
                    if (data.endsWith(")")) {
                        // Returns colored leather, or whatever item was passed in if not leather
                        chestplate = parseColoredArmor(data);
                        continue;
                    }

                    if (MiscUtil.checkIfInt(data)) throw new ShowParseException("Legacy (1.12) numeric IDs are no longer supported.  Please convert to modern names.");
                    chestplate = new ItemStack(Material.valueOf(data)); // Get material

                }
                case 2: { // Leggings

                    // Try to color leather armor
                    if (data.endsWith(")")) {
                        // Returns colored leather, or whatever item was passed in if not leather
                        leggings = parseColoredArmor(data);
                        continue;
                    }

                    if (MiscUtil.checkIfInt(data)) throw new ShowParseException("Legacy (1.12) numeric IDs are no longer supported.  Please convert to modern names.");
                    leggings = new ItemStack(Material.valueOf(data)); // Get material
                }
                case 3: { // Boots

                    // Try to color leather armor
                    if (data.endsWith(")")) {
                        // Returns colored leather, or whatever item was passed in if not leather
                        boots = parseColoredArmor(data);
                        continue;
                    }

                    if (MiscUtil.checkIfInt(data)) throw new ShowParseException("Legacy (1.12) numeric IDs are no longer supported.  Please convert to modern names.");
                    boots = new ItemStack(Material.valueOf(data)); // Get material
                }
                case 4: { // Main Hand
                    if (MiscUtil.checkIfInt(data)) throw new ShowParseException("Legacy (1.12) numeric IDs are no longer supported.  Please convert to modern names.");
                    itemInMainHand = new ItemStack(Material.valueOf(data)); // Get material
                }
                case 5: { // Off Hand
                    if (MiscUtil.checkIfInt(data)) throw new ShowParseException("Legacy (1.12) numeric IDs are no longer supported.  Please convert to modern names.");
                    itemInOffHand = new ItemStack(Material.valueOf(data)); // Get material
                }

            }

        }

        return new ArmorData(head, chestplate, leggings, boots, itemInMainHand, itemInOffHand);
    }

    /**
     * Tries to get a piece of leather armor with color values.
     * @param data Armor and color data. Format: LEATHER_HELMET:data(r,g,b)
     * @return Colored item
     */
    private static ItemStack parseColoredArmor(String data) throws Exception {
        String colors = data.split(":")[1]; // should get "data(r,g,b)"
        colors = colors.replaceFirst("data", ""); // should get "(r,g,b)"
        colors = colors.replaceAll("\\)", "");
        colors = colors.replaceAll("\\(", "");  // should get "r,g,b"

        int red = Integer.parseInt(colors.split(",")[0]);
        int green = Integer.parseInt(colors.split(",")[1]);
        int blue = Integer.parseInt(colors.split(",")[2]);

        if (MiscUtil.checkIfInt(data.split(":")[0])) throw new ShowParseException("Legacy (1.12) numeric IDs are no longer supported.  Please convert to modern names.");
        Material mat = Material.valueOf(data.split(":")[0]); // Get material

        // If can't be colored, throw exception
        if (!mat.toString().toLowerCase().contains("leather")) {
            throw new ShowParseException("Material (" + mat + ") cannot be colored.");
        }

        // Apply colors
        ItemStack temp = new ItemStack(mat, 1);
        LeatherArmorMeta lam = (LeatherArmorMeta) temp.getItemMeta();
        lam.setColor(Color.fromRGB(red, green, blue));
        temp.setItemMeta(lam);

        return temp;
    }
}
