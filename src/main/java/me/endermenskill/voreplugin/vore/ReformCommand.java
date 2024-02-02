package me.endermenskill.voreplugin.vore;

import me.endermenskill.voreplugin.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static me.endermenskill.voreplugin.vore.VoreManager.getPrey;
import static me.endermenskill.voreplugin.vore.VoreManager.isDigeted;

public class ReformCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player)){
            return false;
        }
        Player p = (Player) commandSender;

        if (VoreManager.isDigeted(p)) {
            p.sendTitle(ChatColor.GREEN + "You have been reformed!", "Your items have been returned from your predator's belly.", 0, 60, 20);
            VoreManager.digestedPlayers.remove(p.getUniqueId());
            return true;
        }

        if (args.length < 1) {
            p.sendMessage(Settings.msgPrefix + ChatColor.RED + "Need to specify a player to reform!");
            return false;
        }

        ArrayList<Player> prey = getPrey(p);
        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            p.sendMessage(Settings.msgPrefix + ChatColor.RED + "Invalid player name");
            return true;
        }

        if (!prey.contains(target)) {
            p.sendMessage(Settings.msgPrefix + ChatColor.RED + "You haven't eaten that player");
            return true;
        }

        if (!isDigeted(target)) {
            p.sendMessage(Settings.msgPrefix + ChatColor.RED + "You haven't digested that player");
            return true;
        }

        target.setGameMode(GameMode.SURVIVAL);
        VoreManager.digestedPlayers.remove(p.getUniqueId());
        target.sendTitle(ChatColor.GREEN + "You have been reformed!", "Your items have been returned from your predator's belly.", 0, 60, 20);

        return true;
    }
}
