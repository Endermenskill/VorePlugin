package me.endermenskill.voreplugin.belly;

import me.endermenskill.voreplugin.player.PlayerUtil;
import me.endermenskill.voreplugin.vore.VoreType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

/**
 * Class representing a belly
 */
public class Belly {
    private UUID owner = null;
    private final ConfigurationSection data;


    /**
     * Class constructor to create a belly from scratch
     * @param player Player who you want a belly for
     */
    public Belly (Player player) {
        this.setOwner(player);

        String sectionName = PlayerUtil.getEmptyBellySection(player);
        FileConfiguration playerConfig = PlayerUtil.getPlayerFile(player);
        this.data = playerConfig.getConfigurationSection("bellies." + sectionName);

        this.setLocation(player.getLocation());
        this.setDefaults();
    }

    /**
     * Load the belly data from a ConfigurationSection into a Belly object
     * @param data ConfigurationSection containing valid belly data to load
     */
    public Belly (ConfigurationSection data) {
        String ownerID = data.getString("owner");
        assert ownerID != null;
        this.owner = UUID.fromString(ownerID);

        this.data = data;
    }

    public void setDefaults() {
        this.setType(VoreType.ORAL);
        this.setSwallowMessage("You have been swallowed by <pred>.");
        this.setDigestInitMessage("<pred> starts to digest you, their belly grumbling eagerly.");
        this.setDigestMessage("<prey> got digested by <pred>.");
        this.setReleaseMessage("*The walls around you contract, pushing you out of <pred>'s belly.*");
        this.setBellyEffect(PotionEffectType.SLOW);
        this.setAcidStrength((byte) 1);
    }

    /**
     * Set the belly's owner
     * @param owner new owner
     */
    private void setOwner(Player owner) {
        this.owner = owner.getUniqueId();
        this.data.set("owner", owner.getUniqueId().toString());

        PlayerUtil.savePlayerFile(owner, PlayerUtil.getPlayerFile(owner));
    }

    /**
     * Set the belly's name/id
     * @param name new name
     */
    public void setName(String name) {
        this.data.set("name", name);

        PlayerUtil.savePlayerFile(this.getOwner(), PlayerUtil.getPlayerFile(this.getOwner()));
    }

    /**
     * Set the belly's entrance location
     * @param location new location
     */
    public void setLocation(Location location) {
        assert location.getWorld() != null;
        this.data.set("world", location.getWorld().getUID().toString());

        this.data.set("x", location.getX());
        this.data.set("y", location.getY());
        this.data.set("z", location.getZ());

        this.data.set("yaw", location.getYaw());
        this.data.set("pitch", location.getPitch());

        PlayerUtil.savePlayerFile(this.getOwner(), PlayerUtil.getPlayerFile(this.getOwner()));
    }

    /**
     * Set the belly's vore type
     * @param type new type
     */
    public void setType(VoreType type) {
        this.data.set("type", type.toString());

        PlayerUtil.savePlayerFile(this.getOwner(), PlayerUtil.getPlayerFile(this.getOwner()));
    }

    /**
     * Set the belly's swallow message
     * @param message new message
     */
    public void setSwallowMessage(String message) {
        this.data.set("swallowMessage", message);

        PlayerUtil.savePlayerFile(this.getOwner(), PlayerUtil.getPlayerFile(this.getOwner()));
    }

    /**
     * Set the belly's digestion start message
     * @param message new message
     */
    public void setDigestInitMessage(String message) {
        this.data.set("digestInitMessage", message);

        PlayerUtil.savePlayerFile(this.getOwner(), PlayerUtil.getPlayerFile(this.getOwner()));
    }

    /**
     * Set the belly's digest message
     * @param message new message
     */
    public void setDigestMessage(String message) {
        this.data.set("digestMessage", message);

        PlayerUtil.savePlayerFile(this.getOwner(), PlayerUtil.getPlayerFile(this.getOwner()));
    }

    /**
     * Set the belly's release message
     * @param message new message
     */
    public void setReleaseMessage(String message) {
        this.data.set("releaseMessage", message);

        PlayerUtil.savePlayerFile(this.getOwner(), PlayerUtil.getPlayerFile(this.getOwner()));
    }

    /**
     * Set the belly's ambient effect
     * @param effect new effect
     */
    public void setBellyEffect(PotionEffectType effect) {
        this.data.set("bellyEffect", effect.toString());

        PlayerUtil.savePlayerFile(this.getOwner(), PlayerUtil.getPlayerFile(this.getOwner()));
    }

    /**
     * Set the belly's acid strength
     * @param strength new strength
     */
    public void setAcidStrength(int strength) {
        if (strength < 1 || strength > 255) {
            strength = 1;
        }
        this.data.set("acidStrength", strength);

        PlayerUtil.savePlayerFile(this.getOwner(), PlayerUtil.getPlayerFile(this.getOwner()));
    }

    /**
     * Get the belly's owner
     * @return The belly's owner as a Player object
     */
    public Player getOwner() {
        return Bukkit.getPlayer(this.owner);
    }

    /**
     * Get the belly's name
     * @return The belly's name
     */
    public String getName() {
        return this.data.getString("name");
    }

    /**
     * Get the belly's location
     * Incomplete or corrupted data will be replaced with location 0,0,0 of Bukkit.getWorlds().get(0)
     * @return The belly's location as a Location object
     */
    public Location getLocation() {
        String worldID = this.data.getString("world");
        World world = worldID == null ? Bukkit.getWorlds().get(0) : Bukkit.getWorld(UUID.fromString(worldID));

        String xData = this.data.getString("x");
        String yData = this.data.getString("y");
        String zData = this.data.getString("z");
        double x = xData == null ? 0 : Double.parseDouble(xData);
        double y = yData == null ? 0 : Double.parseDouble(yData);
        double z = zData == null ? 0 : Double.parseDouble(zData);

        String yawData = this.data.getString("yaw");
        String pitchData = this.data.getString("pitch");
        float yaw = yawData == null ? 0 :Float.parseFloat(yawData);
        float pitch = pitchData == null ? 0 : Float.parseFloat(pitchData);

        if (worldID == null || xData == null || yData == null || zData == null) {
            Bukkit.getLogger().warning("[VorePlugin] Error retrieving location data for belly " + this.getName() + ", owned by " + this.getOwner().getUniqueId());
        }

        return new Location(world, x, y, z, yaw, pitch);
    }

    /**
     * Get the belly's vore type
     * @return The belly's vore type as a VoreType value
     */
    public VoreType getType() {
        return VoreType.valueOf(this.data.getString("type"));
    }

    /**
     * Get the belly's swallow message
     * @return The belly's swallow message
     */
    public String getSwallowMessage() {
        return this.data.getString("swallowMessage");
    }

    /**
     * Get the belly's digestion start message
     * @return The belly's digest message
     */
    public String getDigestInitMessage() {
        return this.data.getString("digestInitMessage");
    }

    /**
     * Get the belly's digest message
     * @return The belly's digest message
     */
    public String getDigestMessage() {
        return this.data.getString("digestMessage");
    }

    /**
     * Get the belly's release message
     * @return The belly's release message
     */
    public String getReleaseMessage() {
        return this.data.getString("releaseMessage");
    }

    /**
     * Get the belly's ambient effect
     * @return The belly's ambient efect type as a PotionEffectType value
     */
    public PotionEffectType getBellyEffect() {
        String effect = this.data.getString("bellyEffect");
        if (effect == null) {
            return PotionEffectType.SLOW;
        }
        return PotionEffectType.getByName(effect);
    }

    public byte getAcidStrength() {
        String strength = this.data.getString("acidStrength");
        if (strength == null) {
            return 1;
        }
        return Byte.parseByte(strength);
    }
}