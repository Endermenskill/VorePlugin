package me.endermenskill.voreplugin.vore.digestion;

import me.endermenskill.voreplugin.belly.Belly;
import me.endermenskill.voreplugin.vore.VoreManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DisposalTabCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) {
            return null;
        }
        Player p = (Player) commandSender;
        List<String> list = new ArrayList<>();

        switch (args.length){
            case 0 -> {
                for (Belly belly : VoreManager.getBellies(p)) {
                    list.add(belly.getName());
                }
            }
            case 1 -> {
                for (Player prey: VoreManager.getPrey(p)) {
                    list.add(prey.getDisplayName());
                }
            }
        }

        return list;
    }
}
