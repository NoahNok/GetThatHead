package noahnok.gth.file;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by noahd on 25/06/2017.
 */
public class GetThatHead extends JavaPlugin {

    public void onEnable(){

        this.getLogger().info("Has been enabled!");
        saveDefaultConfig();
    }


    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("gethead") || cmd.getName().equalsIgnoreCase("getthathead")) {
            if (sender instanceof Player){
            if (sender.hasPermission("gth.head")) {
                if (args.length == 0) {
                    sender.sendMessage(getMessage("NO_ARGS"));
                    return true;
                }
                Inventory inv = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', getConfig().getString("inventoryname")));
                int count = 0;
                for (Player p : getServer().getOnlinePlayers()) {
                    if (p.getName().toLowerCase().contains(args[0].toLowerCase())) {
                        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
                        SkullMeta sm = (SkullMeta) skull.getItemMeta();
                        sm.setOwner(p.getName());
                        skull.setItemMeta(sm);
                        inv.addItem(skull);
                        count++;
                    }


                }

                if (inv.getItem(0) == null) {
                    int slot = 0;
                    while (slot < 54) {
                        ItemStack glass = new ItemStack(Material.STAINED_GLASS, 1, (short) 14);
                        ItemMeta m = glass.getItemMeta();
                        m.setDisplayName(getMessage("NO_HEADS"));
                        glass.setItemMeta(m);
                        inv.setItem(slot, glass);
                        slot++;
                    }
                }
                sender.sendMessage(getMessage("WAITING_ONHEADS") + " Loading at 1 Head/1.5s");
                final Inventory tinv = inv;
                final Player s = (Player) sender;
                Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(this, new Runnable() {
                    public void run() {
                        s.openInventory(tinv);
                    }
                }, 30L * count);
            } else {
                sender.sendMessage(getMessage("NO_PERM"));
                return true;
            }
        }else{sender.sendMessage("Only a player can run this!");}
            return true;

        }
        if (cmd.getName().equalsIgnoreCase("getheads") || cmd.getName().equalsIgnoreCase("getthoseheads")) {
            if (sender instanceof Player){
                if (sender.hasPermission("gth.multihead")) {
                    if (args.length == 0) {
                        sender.sendMessage(getMessage("MULTIHEAD_INCORRECT_FORMAT"));
                        return true;
                    }
                    Player p = (Player) sender;
                    getHeads(args[0]);

                    final String heads = args[0];
                    final Player r = p;
                    p.sendMessage(getMessage("WAITING_ONHEADS") + " Loading at 1 Head/1.5s");
                    Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(this, new Runnable() {
                        public void run() {
                            r.openInventory(getHeads(heads));
                        }
                    }, 30L * args[0].split(",").length);

                    return true;
                } else {
                    sender.sendMessage(getMessage("NO_PERM"));
                    return true;
                }
        }else{sender.sendMessage("Only a player can run this!");}
        }
        if (cmd.getName().equalsIgnoreCase("gthreload")){
            if (sender.hasPermission("gth.reload")){
                reloadConfig();
                sender.sendMessage(getMessage("PLUGIN_RELOAD"));
                return true;
            } else {
                sender.sendMessage(getMessage("NO_PERM"));
                return true;
            }

        }

            return true;

    }

    public Inventory getHeads(String heads){
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', getConfig().getString("inventoryname")));
        for (String playername : heads.split(",")){

            ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            SkullMeta sm = (SkullMeta) skull.getItemMeta();
            sm.setOwner(playername);

            skull.setItemMeta(sm);
            inv.addItem(skull);


        }
        return inv;
    }

    public String getMessage(String message){
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages." + message));
    }
}


