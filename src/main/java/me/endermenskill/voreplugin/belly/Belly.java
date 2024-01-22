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
    UUID owner;
    private String name;
    private Location location;
    private VoreType type;
    private String swallowMessage;
    private String digestInitMessage;
    private String digestMessage;
    private String releaseMessage;
    private String bellyEffect;
    private int acidStrength;


    /**
     * Class constructor to create a belly from scratch
     * @param player Player who you want a belly for
     */
    public Belly (Player player) {
        this.init(player);
        this.setLocation(player.getLocation());
        this.setType(VoreType.ORAL);
        this.setSwallowMessage("You have been swallowed by <pred>.");
        this.setDigestInitMessage("<pred> starts to digest you, their belly grumbling eagerly.");
        this.setDigestMessage("<prey> got digested by <pred>.");
        this.setReleaseMessage("*The walls around you contract, pushing you out of <pred>'s belly.*");
        this.setBellyEffect("SLOW");
        this.setAcidStrength((byte) 1);
    }

    /**
     * Alternate class constructor to instantly load a new object with a belly's data
     * @param data ConfigurationSection containing valid belly data to load
     */
    public Belly (ConfigurationSection data) {
        this.load(data);
    }

    /**
     * Method to initialise the belly class' variables
     * @param owner Player for whom to initialise the class
     */
    private void init(Player owner) {
        String name = "new Belly";

        FileConfiguration playerFile = PlayerUtil.getPlayerFile(owner);
        ConfigurationSection bellySection = playerFile.getConfigurationSection("bellies");
        assert bellySection != null;

        String key = PlayerUtil.getBellyConfigurationSection(owner, name);
        if (key == null) {
            key = PlayerUtil.getEmptyBellySection(owner);
            if (key == null) {
                Bukkit.getLogger().warning("cannot create belly for " + owner.getName());
                return;
            }
        }

        this.owner = owner.getUniqueId();
        this.name = name;
        bellySection.set(key + ".owner", owner.getUniqueId().toString());
        bellySection.set(key + ".name", name);

        PlayerUtil.savePlayerFile(owner, playerFile);
    }

    /**
     * Set the belly's owner
     * @param owner new owner
     */
    private void setOwner(Player owner) {
        FileConfiguration playerFile = PlayerUtil.getPlayerFile(owner);
        String dataSection = PlayerUtil.getBellyConfigurationSection(owner, this.name);
        ConfigurationSection bellyData = playerFile.getConfigurationSection("bellies." + dataSection);
        assert bellyData != null;

        this.owner = owner.getUniqueId();
        bellyData.set("owner", owner.getUniqueId().toString());

        PlayerUtil.savePlayerFile(this.getOwner(), playerFile);
    }

    /**
     * Set the belly's name/id
     * @param name new name
     */
    public void setName(String name) {
        FileConfiguration playerFile = PlayerUtil.getPlayerFile(this.getOwner());
        String dataSection = PlayerUtil.getBellyConfigurationSection(this.getOwner(), this.name);
        ConfigurationSection bellyData = playerFile.getConfigurationSection("bellies." + dataSection);
        assert bellyData != null;

        this.name = name;
        bellyData.set("name", name);

        PlayerUtil.savePlayerFile(this.getOwner(), playerFile);
    }

    /**
     * Set the belly's entrance location
     * @param location new location
     */
    public void setLocation(Location location) {
        FileConfiguration playerFile = PlayerUtil.getPlayerFile(this.getOwner());
        String dataSection = PlayerUtil.getBellyConfigurationSection(this.getOwner(), this.name);
        ConfigurationSection bellyData = playerFile.getConfigurationSection("bellies." + dataSection);
        assert bellyData != null;
        this.location = location;

        bellyData.set("world", location.getWorld().getUID().toString());

        bellyData.set("x", location.getX());
        bellyData.set("y", location.getY());
        bellyData.set("z", location.getZ());

        bellyData.set("yaw", location.getYaw());
        bellyData.set("pitch", location.getPitch());

        PlayerUtil.savePlayerFile(this.getOwner(), playerFile);
    }

    /**
     * Set the belly's vore type
     * @param type new type
     */
    public void setType(VoreType type) {
        FileConfiguration playerFile = PlayerUtil.getPlayerFile(this.getOwner());
        String dataSection = PlayerUtil.getBellyConfigurationSection(this.getOwner(), this.name);
        ConfigurationSection bellyData = playerFile.getConfigurationSection("bellies." + dataSection);
        assert bellyData != null;

        this.type = type;
        bellyData.set("type", type.toString());

        PlayerUtil.savePlayerFile(this.getOwner(), playerFile);
    }

    /**
     * Set the belly's swallow message
     * @param message new message
     */
    public void setSwallowMessage(String message) {
        FileConfiguration playerFile = PlayerUtil.getPlayerFile(this.getOwner());
        String dataSection = PlayerUtil.getBellyConfigurationSection(this.getOwner(), this.name);
        ConfigurationSection bellyData = playerFile.getConfigurationSection("bellies." + dataSection);
        assert bellyData != null;

        this.swallowMessage = message;
        bellyData.set("swallowMessage", message);

        PlayerUtil.savePlayerFile(this.getOwner(), playerFile);
    }

    /**
     * Set the belly's digestion start message
     * @param message new message
     */
    public void setDigestInitMessage(String message) {
        FileConfiguration playerFile = PlayerUtil.getPlayerFile(this.getOwner());
        String dataSection = PlayerUtil.getBellyConfigurationSection(this.getOwner(), this.name);
        ConfigurationSection bellyData = playerFile.getConfigurationSection("bellies." + dataSection);
        assert bellyData != null;

        this.digestInitMessage = message;
        bellyData.set("digestInitMessage", message);

        PlayerUtil.savePlayerFile(this.getOwner(), playerFile);
    }

    /**
     * Set the belly's digest message
     * @param message new message
     */
    public void setDigestMessage(String message) {
        FileConfiguration playerFile = PlayerUtil.getPlayerFile(this.getOwner());
        String dataSection = PlayerUtil.getBellyConfigurationSection(this.getOwner(), this.name);
        ConfigurationSection bellyData = playerFile.getConfigurationSection("bellies." + dataSection);
        assert bellyData != null;

        this.digestMessage = message;
        bellyData.set("digestMessage", message);

        PlayerUtil.savePlayerFile(this.getOwner(), playerFile);
    }

    /**
     * Set the belly's release message
     * @param message new message
     */
    public void setReleaseMessage(String message) {
        FileConfiguration playerFile = PlayerUtil.getPlayerFile(this.getOwner());
        String dataSection = PlayerUtil.getBellyConfigurationSection(this.getOwner(), this.name);
        ConfigurationSection bellyData = playerFile.getConfigurationSection("bellies." + dataSection);
        assert bellyData != null;

        this.releaseMessage = message;
        bellyData.set("releaseMessage", message);

        PlayerUtil.savePlayerFile(this.getOwner(), playerFile);
    }

    /**
     * Set the belly's ambient effect
     * @param effect new effect
     */
    public void setBellyEffect(String effect) {
        FileConfiguration playerFile = PlayerUtil.getPlayerFile(this.getOwner());
        String dataSection = PlayerUtil.getBellyConfigurationSection(this.getOwner(), this.name);
        ConfigurationSection bellyData = playerFile.getConfigurationSection("bellies." + dataSection);

        assert bellyData != null;
        effect = effect.toUpperCase();

        if (PotionEffectType.getByName(effect) == null) {
            effect = "SLOW";
        }

        this.bellyEffect = effect;
        bellyData.set("bellyEffect", effect);

        PlayerUtil.savePlayerFile(this.getOwner(), playerFile);
    }

    /**
     * Set the belly's acid strength
     * @param strength new strength
     */
    public void setAcidStrength(int strength) {
        FileConfiguration playerFile = PlayerUtil.getPlayerFile(this.getOwner());
        String dataSection = PlayerUtil.getBellyConfigurationSection(this.getOwner(), this.name);
        ConfigurationSection bellyData = playerFile.getConfigurationSection("bellies." + dataSection);
        assert bellyData != null;

        if (strength < 1) {
            strength = 1;
        }

        this.acidStrength = strength;
        bellyData.set("acidStrength", strength);

        PlayerUtil.savePlayerFile(this.getOwner(), playerFile);
    }

    /**
     * save the belly's data
     */
    public void save() {
        this.setOwner(this.getOwner());
        this.setName(this.name);
        this.setLocation(this.location);
        this.setType(this.type);
        this.setSwallowMessage(this.swallowMessage);
        this.setDigestInitMessage(this.digestInitMessage);
        this.setDigestMessage(this.digestMessage);
        this.setReleaseMessage(this.releaseMessage);
        this.setBellyEffect(this.bellyEffect);
        this.setAcidStrength(this.acidStrength);
    }

    /**
     * Load a belly's data into the class
     * @param data configurationSection containing the data of the belly to load
     */
    public void load(ConfigurationSection data) {

        this.owner = UUID.fromString(data.getString("owner"));

        this.name = data.getString("name");

        World world = Bukkit.getWorld(UUID.fromString(String.valueOf(data.get("world"))));

        double locX = data.getDouble("x");
        double locY = data.getDouble("y");
        double locZ = data.getDouble("z");

        String yawStr = data.getString("yaw");
        String pitchStr = data.getString("pitch");
        float yaw = (yawStr == null) ? 0F : Float.parseFloat(yawStr);
        float pitch = (pitchStr == null) ? 0F : Float.parseFloat(pitchStr);

        this.location = new Location(world, locX, locY, locZ, yaw, pitch);
        this.type = VoreType.valueOf(data.getString("type"));

        if (data.getString("swallowMessage") == null) {
            Bukkit.getLogger().warning("Could not find swallowMessage for belly " + this.name + " from player " + this.getOwner().getName() + ", UUID " + this.owner);
            this.swallowMessage = "You have been swallowed.";
        } else {
            this.swallowMessage = data.getString("swallowMessage");
        }

        if (data.getString("digestInitMessage") == null) {
            Bukkit.getLogger().warning("Could not find digestInitMessage for belly " + this.name + " from player " + this.getOwner().getName() + ", UUID " + this.owner);
            this.digestInitMessage = "Your captor starts to digest you, their belly grumbling eagerly.";
        }else {
            this.digestInitMessage = data.getString("digestInitMessage");
        }

        if (data.getString("digestMessage") == null) {
            Bukkit.getLogger().warning("Could not find digestMessage for belly " + this.name + " from player " + this.getOwner().getName() + ", UUID " + this.owner);
            this.digestMessage = "<prey> got digested by <pred>";
        }else {
            this.digestMessage = data.getString("digestMessage");
        }

        if (data.getString("releaseMessage") == null) {
            Bukkit.getLogger().warning("Could not find releaseMessage for belly " + this.name + " from player " + this.getOwner().getName() + ", UUID " + this.owner);
            this.releaseMessage = "The walls around you contract, pushing you out of your pred's belly.";
        }else {
            this.releaseMessage = data.getString("releaseMessage");
        }

        if (data.getString("bellyEffect") == null) {
            Bukkit.getLogger().warning("Could not find bellyEffect for belly " + this.name + " from player " + this.getOwner().getName() + ", UUID " + this.owner);
            this.bellyEffect = "SLOW";
        } else {
            this.bellyEffect = data.getString("bellyEffect");
        }

        if (data.getString("acidStrength") == null) {
            Bukkit.getLogger().warning("Could not find acid strength for belly " + this.name + " from player " + this.getOwner().getName() + ", UUID " + this.owner);
            this.acidStrength = 1;
        } else {
            this.acidStrength = data.getInt("acidStrength");
        }
    }

    /**
     * Get the belly's swallow message
     * @param prey Prey to get the message for, needed for the dynamic message
     * @return The belly's swallow message
     */
    public String getSwallowMessage(Player prey) {
        String message = this.swallowMessage;
        if (message == null) {
            message = "You have been swallowed by <pred>.";
        }

        message = message.replaceAll("<pred>", this.getOwner().getDisplayName() + "§r");
        message = message.replaceAll("<prey>", prey.getDisplayName() + "§r");
        message = message.replaceAll("<belly>", this.name + "§r");

        return message;
    }

    /**
     * Get the belly's digestion start message
     * @param prey Prey to get the message for, needed for dynamic message
     * @return The belly's digest message
     */
    public String getDigestInitMessage(Player prey) {
        String message = this.digestInitMessage;
        if (message == null) {
            message = "<pred> starts to digest you, their belly grumbling eagerly.";
        }

        message = message.replaceAll("<pred>", this.getOwner().getDisplayName() + "§r");
        message = message.replaceAll("<prey>", prey.getDisplayName() + "§r");
        message = message.replaceAll("<belly>", this.name + "§r");

        return message;
    }

    /**
     * Get the belly's digest message
     * @param prey Prey to get the message for, needed for dynamic message
     * @return The belly's digest message
     */
    public String getDigestMessage(Player prey) {
        String message = this.digestMessage;
        if (message == null) {
            message = "<prey> got digested by <pred>.";
        }

        message = message.replaceAll("<pred>", this.getOwner().getDisplayName() + "§r");
        message = message.replaceAll("<prey>", prey.getDisplayName() + "§r");
        message = message.replaceAll("<belly>", this.name + "§r");

        return message;
    }

    /**
     * Get the belly's release message
     * @param prey Prey to get the message for needed for dynamic message
     * @return The belly's release message
     */
    public String getReleaseMessage(Player prey) {
        String message = this.releaseMessage;
        if (message == null) {
            message = "*The walls around you contract, pushing you out of <pred>'s belly.*";
        }

        message = message.replaceAll("<pred>", this.getOwner().getDisplayName() + "§r");
        message = message.replaceAll("<prey>", prey.getDisplayName() + "§r");
        message = message.replaceAll("<belly>", this.name + "§r");

        return message;
    }

    public Player getOwner() {
        return Bukkit.getPlayer(this.owner);
    }
}