package me.endermenskill.voreplugin.belly;

import me.endermenskill.voreplugin.Settings;
import me.endermenskill.voreplugin.player.PlayerRank;
import me.endermenskill.voreplugin.player.PlayerUtil;
import me.endermenskill.voreplugin.vore.VoreManager;
import me.endermenskill.voreplugin.vore.VoreType;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Class handling the /setbelly command
 */
public class SetBellyCommand implements CommandExecutor {

    /**
     * Main function to handle the /setbelly command
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true if the command was completed successfully, false otherwise.
     */
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Settings.msgPrefix + " §cYou cannot run that command from the console.");
            return true;
        }

        Player p = (Player) sender;

        if (PlayerUtil.getPlayerRank(p) == PlayerRank.PREY) {
            sender.sendMessage(Settings.msgPrefix + " §cPrey can't set bellies.");
            return true;
        }

        if (!VoreManager.getPrey(p).isEmpty()) {
            p.sendMessage(Settings.msgPrefix + " §cCannot set bellies while having swallowed prey.");
        }

        if (args.length < 1) {
            p.sendMessage(Settings.msgPrefix + " §cExpected argument at position 0 but found none.");
            return false;
        }

        if (args[0] == null) {
            return false;
        }

        //check if belly max is exceeded
        int bellyAmount = 0;
        try {
            bellyAmount = VoreManager.getBellies(p).size();
        }
        catch (NullPointerException ignored) {}

        if (bellyAmount >= 64) {
            sender.sendMessage(Settings.msgPrefix + " §cYou can only have up to 64 bellies set at once.");
            return true;
        }

        Belly belly = new Belly(p);
        belly.setDefaults();
        belly.setName(args[0]);

        Location loc = p.getLocation();
        belly.setLocation(loc);

        if (args.length >= 2) {
            try {
                VoreType type = VoreType.valueOf(args[1]);
                belly.setType(type);
            } catch (Exception e) {
                p.sendMessage(Settings.msgPrefix + " §cInvalid vore type" + args[1] + "§rReplaced missing optiona argument with default value of " + VoreType.ORAL + ".");
            }
        }

        sender.sendMessage(Settings.msgPrefix + " §aSuccessfully set the belly \"" + belly.getName() + "\" to your location. May it be the home of many snacks~");
        return true;
    }
}
