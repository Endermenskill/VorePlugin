package me.endermenskill.voreplugin.vore;

import me.endermenskill.voreplugin.belly.Belly;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Class handling the /release command
 */
public class ReleaseCommand implements CommandExecutor{

    /**
     * Main function to handle the /release command
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true if the command has been executed successfully, false otherwise
     */
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§8[§b§lVorePlugin§8] §cYou cannot run that command from the console.");
            return true;
        }
        if (args.length != 1) {
            return false;
        }

        Player p = (Player)sender;

        Player prey = Bukkit.getPlayer(args[0]);

        ArrayList<Player> targets = new ArrayList<>();
        boolean releaseByOP = false;
        if (args[0].equals("all")) {
            targets = VoreManager.getPrey(p);
        } else {
            if (prey == null) {
                p.sendMessage("§8[§b§lVorePlugin§8] §cThere is no player online called \"" + args[0] + "\".");
                return true;
            }

            if (VoreManager.voredPlayers.get(prey.getUniqueId()).getOwner() == p || Bukkit.getOperators().contains(p)){
                targets.add(prey);
                releaseByOP = true;
            }
        }

        for (Player target : targets) {
            if (target == null) {
                p.sendMessage("§8[§b§lVorePlugin§8] §cThere is no player online called \"" + args[0] + "\"."); return true;
            }

            UUID targetID = target.getUniqueId();

            if (!VoreManager.voredPlayers.containsKey(targetID)) {
                p.sendMessage("§8[§b§lVorePlugin§8] §c" + target.getDisplayName() + " isn't in your belly. Maybe they escaped in your sleep?");
                return true;
            }

            Belly belly = VoreManager.voredPlayers.get(targetID);
            Location release = belly.location;
            if (releaseByOP) {
                release = p.getLocation();
            }

            PotionEffectType bellyEffectType = PotionEffectType.getByName(belly.bellyEffect);
            if (bellyEffectType == null) bellyEffectType = PotionEffectType.SLOW;
            target.removePotionEffect(bellyEffectType);

            target.teleport(release);
            VoreManager.voredPlayers.remove(targetID);
            p.sendMessage("§8[§b§lVorePlugin§8] §a" + target.getDisplayName() + " has been freed from your belly. For now at least.");
            target.sendMessage(belly.getReleaseMessage(target));
        }
        return true;
    }
}
