package me.endermenskill.voreplugin.vore.digestion;

import me.endermenskill.voreplugin.belly.Belly;
import me.endermenskill.voreplugin.vore.VoreManager;
import me.endermenskill.voreplugin.vore.VoreType;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DisposalCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player p = (Player) sender;
        VoreType type = null;
        if (args.length >= 1){
            Belly belly = VoreManager.getBelly(p, args[0]);
            if (belly != null) {
                type = belly.getType();
            }
        }

        Player prey = null;
        if (args.length >= 2) {
            prey = Bukkit.getPlayer(args[1]);
        }

        ItemStack disposalItem = getDisposalItem(type, prey);
        p.getInventory().addItem(disposalItem);

        return true;
    }

    private ItemStack getDisposalItem(VoreType type, Player p){
        if (type == null) {
            type = VoreType.ORAL;
        }
        Pair<Material, String> itemData = getDisposalItemMaterial(type);

        ItemStack item = new ItemStack(itemData.getLeft());
        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.DARK_BLUE + itemData.getRight());

        List<String> lore = new ArrayList<>();
        String addition = "someone you digested";
        if (p != null) {
            addition = p.getDisplayName();
        }
        lore.add("This used to be " + addition + "~");
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    private Pair<Material, String> getDisposalItemMaterial(VoreType type) {
        return switch (type) {
            case COCK, UNBIRTH -> Pair.of(Material.WHITE_DYE, "cum");
            case BREAST -> Pair.of(Material.WHITE_DYE, "milk");
            default -> Pair.of(Material.BROWN_DYE, "shit");
        };
    }
}
