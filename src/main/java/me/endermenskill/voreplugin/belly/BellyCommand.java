package me.endermenskill.voreplugin.belly;

import me.endermenskill.voreplugin.Settings;
import me.endermenskill.voreplugin.vore.VoreManager;
import me.endermenskill.voreplugin.vore.VoreType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class handling the /belly command
 */
public class BellyCommand implements CommandExecutor {

    /**
     * main function to handle the command
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true if the command was executed successfully, false otherwise.
     */
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Settings.msgPrefix + " §cYou cannot run that command from the console.");
            return false;
        }

        Player p = (Player)sender;

        if (VoreManager.getPrey(p).size() > 0) {
            p.sendMessage(Settings.msgPrefix + " §cCannot edit bellies while they are filled.");
            return true;
        }

        if (args.length < 1 || args[0] == null) {
            String bellies = getBellies(p).toString();
            p.sendMessage(Settings.msgPrefix + " §cYou did not specify wich belly to edit. Available bellies are " + bellies);
            return true;
        }

        Belly bellyToEdit = VoreManager.getBelly(p, args[0]);
        assert bellyToEdit != null;

        switch (args[1]) {
            case "swallow_message" -> {
                StringBuilder message = new StringBuilder();
                for (int i = 2; i < args.length; i++) message.append(args[i]).append(" ");
                return setMessage(p, bellyToEdit, message.toString().trim());
            }

            case "digest_message" -> {
                StringBuilder message = new StringBuilder();
                for (int i = 2; i < args.length; i++) message.append(args[i]).append(" ");
                return setDigestMessage(p, bellyToEdit, message.toString().trim());
            }

            case "digest_start_message" -> {
                StringBuilder message = new StringBuilder();
                for (int i = 2; i < args.length; i++) message.append(args[i]).append(" ");
                return setDigestInitMessage(p, bellyToEdit, message.toString().trim());
            }

            case "release_message" -> {
                StringBuilder message = new StringBuilder();
                for (int i = 2; i < args.length; i++) message.append(args[i]).append(" ");
                return setReleaseMessage(p, bellyToEdit, message.toString().trim());
            }

            case "vore_type" -> {
                return setType(p, bellyToEdit, args[2]);
            }


            case "acid_strength" -> {
                return setAcidStrength(p, bellyToEdit, Integer.parseInt(args[2]));
            }

            case "ambient_effect" -> {
                return setEffect(p, bellyToEdit, args[2]);
            }

            case "delete" -> {
                return deleteBelly(p, bellyToEdit);
            }
        }
        return true;
    }

    /**
     * Get a player's bellies in the form of an ArrayList
     * @param p Player to get the bellies of
     * @return ArrayList containing the player's bellies, wil be empty if hey have none.
     */
    ArrayList<String> getBellies(Player p) {
        ArrayList<Belly> bellies = VoreManager.getBellies(p);
        ArrayList<String> namesSet = new ArrayList<>();

        for (Belly belly : bellies) {
            namesSet.add(belly.getName());
        }

        return namesSet;
    }

    /**
     * Private function to se a belly's swallow message.
     * @param p Player whose belly to edit
     * @param belly Name of the belly to edit
     * @param message New swallow message to set
     * @return true
     */
    private boolean setMessage(Player p, Belly belly, String message) {
        belly.setSwallowMessage(message);
        p.sendMessage(Settings.msgPrefix + " §aSuccessfully set \"" + belly.getName() + "\" 's swallow message to \"" + belly.getSwallowMessage() + "\".");
        return true;
    }

    /**
     * Private function to set a belly's digest message.
     * @param p Player whose belly to edit
     * @param belly Name of the belly to edit
     * @param message New digestion message to set
     * @return true
     */
    private boolean setDigestMessage(Player p, Belly belly, String message) {
        belly.setDigestMessage(message);
        p.sendMessage(Settings.msgPrefix + " §aSuccessfully set \"" + belly.getName() + "\" 's digest message to \"" + belly.getDigestMessage() + "\".");
        return true;
    }

    /**
     * Private function to set a belly's digestion start message.
     * @param p Player whose belly to edit
     * @param belly Name of the belly to edit
     * @param message New digestion message to set
     * @return true
     */
    private boolean setDigestInitMessage(Player p, Belly belly, String message) {
        belly.setDigestInitMessage(message);
        p.sendMessage(Settings.msgPrefix + " §aSuccessfully set \"" + belly.getName() + "\" 's digestion start message to \"" + belly.getDigestInitMessage() + "\".");
        return true;
    }

    /**
     * Private function to set a belly's release message.
     * @param p Player whose belly to edit
     * @param belly Name of the belly to edit
     * @param message New swallow message to set
     * @return true
     */
    private boolean setReleaseMessage(Player p, Belly belly, String message) {
        belly.setReleaseMessage(message);
        p.sendMessage(Settings.msgPrefix + " §aSuccessfully set \"" + belly.getName() + "\" 's release message to \"" + belly.getReleaseMessage() + "\".");
        return true;
    }

    /**
     * Private function to set a belly's vore type
     * @param p Player whose belly to edit
     * @param belly Name of the belly to edit
     * @param type Name of the vore type to set
     * @return true
     */
    private boolean setType(Player p, Belly belly, String type) {
        try {
            VoreType voreType = VoreType.valueOf(type);
            belly.setType(voreType);
        }
        catch (IllegalArgumentException e) {
            p.sendMessage(Settings.msgPrefix + " §c\"" + type + "\" is not a valid vore type. Available types are " + Arrays.asList(VoreType.values()));
            return true;
        }
        p.sendMessage(Settings.msgPrefix + " §aSuccessfully set \"" + belly.getName() + "\" 's type to \"" + belly.getType().toString() + "\".");

        return true;
    }

    /**
     * Private function to set a belly's acid strength
     * @param p Player whose belly to edit
     * @param belly Name of the belly to edit
     * @param strength Int of the acid strength
     * @return true
     */
    private boolean setAcidStrength(Player p, Belly belly, int strength) {
        if (strength <= 0) {
            p.sendMessage(Settings.msgPrefix + " §cCannot have acid strength below 1.");
            return true;
        }

        belly.setAcidStrength(strength);
        p.sendMessage(Settings.msgPrefix + " §aSuccessfully set \"" + belly.getName() + "\" 's acid strength to \"" + belly.getAcidStrength() + "\".");
        return true;
    }

    /**
     * Private function to set a belly's ambient effect
     * @param p Player whose belly to edit
     * @param belly Name of the belly to edit
     * @param type Name of the potion effect type to set
     * @return true
     */
    private boolean setEffect(Player p, Belly belly, String type) {
        PotionEffectType effect = PotionEffectType.getByName(type.toUpperCase());

        if (effect == null) {
            p.sendMessage(Settings.msgPrefix + " §c\"" + type + "\" is not a valid effect.");
            return true;
        }

        belly.setBellyEffect(effect);
        p.sendMessage(Settings.msgPrefix + " §aSuccessfully set \"" + belly.getName() + "\" 's ambient effect to \"" + belly.getBellyEffect().toString() + "\".");
        return true;
    }

    /**
     * Private function to delete a belly
     * @param p Player whose belly to delete
     * @param belly Belly to delete
     * @return true
     */
    private boolean deleteBelly(Player p, Belly belly) {
        if (belly == null) {
            p.sendMessage(Settings.msgPrefix + " §cCould not find selected belly.");
            return true;
        }

        p.sendMessage(Settings.msgPrefix + " §eDeleting belly \"" + belly.getName() + "\"...");
        VoreManager.deleteBelly(belly);
        p.sendMessage(Settings.msgPrefix + " §aDeleted belly \"" + belly.getName() + "\"");
        return true;
    }
}
