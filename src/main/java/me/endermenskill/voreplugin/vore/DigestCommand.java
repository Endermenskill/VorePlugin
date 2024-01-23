package me.endermenskill.voreplugin.vore;

import me.endermenskill.voreplugin.Settings;
import me.endermenskill.voreplugin.player.PlayerRank;
import me.endermenskill.voreplugin.belly.Belly;
import me.endermenskill.voreplugin.player.PlayerUtil;
import me.endermenskill.voreplugin.stats.VoreStats;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Class handling the /digest command
 */
public class DigestCommand implements CommandExecutor {

    /**
     * Main function handling the /digest command
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true if the command has been executed successfully, false otherwise
     */
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Settings.msgPrefix + " §cYou cannot run that command from the console.");
            return true;
        }

        if (args.length < 1) {
            return false;
        }

        Player p = (Player)sender;
        Player prey = Bukkit.getPlayer(args[0]);
        ArrayList<Player> targets = new ArrayList<>();

        if (args[0].equals("all")) {
            targets = VoreManager.getPrey(p);
        } else {
            targets.add(prey);
        }

        for (Player target : targets) {
            if (target == null) {
                p.sendMessage(Settings.msgPrefix + " §cThere is no player online called \"" + args[0] + "\".");
                return true;
            }

            if (PlayerUtil.getPlayerRank(target) == PlayerRank.PREDATOR) {
                p.sendMessage(Settings.msgPrefix + " §cYou cannot digest predator " + target.getDisplayName() + ".");
                return true;
            }

            if (target.isInvulnerable()) {
                p.sendMessage(Settings.msgPrefix + " §c" + target.getDisplayName() + " is invulnerable. Are they in creative or god mode?");
                return true;
            }

            Belly belly = VoreManager.voredPlayers.get(target.getUniqueId());

            PotionEffect digest = new PotionEffect(PotionEffectType.WITHER, 6000, belly.getAcidStrength(), false, false, false);
            PotionEffect saturate = new PotionEffect(PotionEffectType.SATURATION, 6000, 1, false, false, true);

            target.addPotionEffect(digest);
            p.addPotionEffect(saturate);

            p.sendMessage(Settings.msgPrefix + belly.getDigestInitMessage());
            p.sendMessage(Settings.msgPrefix + " §cDigesting " + target.getDisplayName());
            target.sendMessage(belly.getDigestInitMessage());

            VoreStats.incrementPreyDigested(p);
            VoreStats.incrementTimesDigested(target);
        }
        return true;
    }
}
