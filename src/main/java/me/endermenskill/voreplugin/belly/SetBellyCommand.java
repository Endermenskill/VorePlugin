package me.endermenskill.voreplugin.belly;

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

import java.util.Arrays;

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
            sender.sendMessage("§8[§b§lVorePlugin§8] §cYou cannot run that command from the console.");
            return true;
        }

        Player p = (Player) sender;

        if (PlayerUtil.getPlayerRank(p) == PlayerRank.PREY) {
            sender.sendMessage("§8[§b§lVorePlugin§8] §cPrey can't set bellies.");
            return true;
        }

        if (VoreManager.getPrey(p).size() > 0) {
            p.sendMessage("§8[§b§lVorePlugin§8] §cCannot set bellies while having swallowed prey.");
        }

        if (args.length < 1) {
            p.sendMessage("§8[§b§lVorePlugin§8] §cExpected argument at position 0 but found none.");
            return false;
        }

        if (args[0] == null) {
            return false;
        }

        Belly belly = new Belly(p);
        belly.setName(args[0]);

        Location loc = p.getLocation();
        belly.setLocation(loc);
        belly.setType(VoreType.ORAL);

        try {
            VoreType type = VoreType.valueOf(args[1]);
            belly.setType(type);
        }
        catch (Exception e) {
            p.sendMessage("§8[§b§lVorePlugin§8] §cInvalid vore type. §rAvailable types are §a" + Arrays.toString(VoreType.values()));
            return true;
        }

        int bellyAmount = 0;
        try {
            bellyAmount = VoreManager.getBellies(p).size();
        }
        catch (NullPointerException ignored) {}

        if (bellyAmount >= 64) {
            sender.sendMessage("§8[§b§lVorePlugin§8] §cYou can only have up to 64 bellies set at once.");
            return true;
        }

        belly.save();

        sender.sendMessage("§8[§b§lVorePlugin§8] §aSuccessfully set the belly \"" + belly.name + "\" to your location. May it be the home of many snacks~");
        VoreManager.reloadPlayerBellies(p);
        return true;
    }
}
