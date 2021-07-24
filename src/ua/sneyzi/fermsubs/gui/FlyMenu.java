package ua.sneyzi.fermsubs.gui;

import org.bukkit.entity.*;
import org.bukkit.plugin.*;

import net.minecraft.server.v1_12_R1.ItemStack;
import ua.sneyzi.fermsubs.Main;
import ua.sneyzi.fermsubs.User;
import ua.sneyzi.fermsubs.gui.api.GUI;
import ua.sneyzi.fermsubs.gui.api.ItemUtil;
import ua.sneyzi.fermsubs.utils.Util;

import java.util.List;

import org.bukkit.*;

public class FlyMenu {
	private GUI gui;

	public FlyMenu(final Player player) {
		this.gui = new GUI(Util.getMessage("fly.GUI.headline"), 5);
		player.openInventory(this.gui.getInventory());
		Bukkit.getScheduler().runTaskAsynchronously((Plugin) Main.getInstance(), () -> this.generate(player));
	}

	public void generate(Player player) {
		String enabled = Main.getInstance().getConfig().getString("messages.working.enabled").replace("&", "§");
		String disabled = Main.getInstance().getConfig().getString("messages.working.disabled").replace("&", "§");
		long timeLeft = User.hasSubscription(player.getName().toLowerCase(), "fly") - System.currentTimeMillis() / 1000;

		Material flyId = Material.getMaterial(Main.getInstance().getConfig().getInt("fly.GUI.fly.id"));
		int flySlot = Main.getInstance().getConfig().getInt("fly.GUI.fly.slot");
		String flyName = Main.getInstance().getConfig().getString("fly.GUI.fly.name").replace("&", "§");
		List<String> flyLore = Main.getInstance().getConfig().getStringList("fly.GUI.fly.lores");
		for (int l = 0; l < flyLore.size(); ++l) {
			flyLore.set(l, flyLore.get(l).replace("&", "§").replace("%working%", player.getAllowFlight() ? enabled : disabled));
		}

		this.gui.addItem(flySlot, ItemUtil.createItemStack(flyId, flyName, flyLore), new GUI.InventoryAction(p -> {		
			if (Main.getInstance().cooldown.cd.containsKey(player.getName().toLowerCase()+"fly")) {
				p.sendMessage(Util.getMessage("fly.msg.cooldown")
						.replace("%time%", Main.getInstance().cooldown.cd.get(player.getName().toLowerCase()+"fly").toString())
						.replace("%prefix%", Util.getMessage("fly.msg.prefix")));
				p.closeInventory();
				return;
			}
	        
			if(User.hasSubscription(player.getName().toLowerCase(), "fly") > 0) {
				if (player.getAllowFlight()) {
					player.setAllowFlight(false);
					//player.setFlying(false);
					player.sendMessage(Util.getMessage("fly.msg.disabled").replace("%prefix%", Util.getMessage("fly.msg.prefix")));
				} else {
					player.setAllowFlight(true);
					//player.setFlying(true);
					player.sendMessage(Util.getMessage("fly.msg.enabled").replace("%prefix%", Util.getMessage("fly.msg.prefix")));
				}
			} else {
				player.sendMessage(Util.getMessage("fly.msg.buy").replace("%prefix%", Util.getMessage("fly.msg.prefix")));
			}
			
            Main.getInstance().cooldown.cd.put(player.getName().toLowerCase()+"fly", Main.getInstance().getConfig().getLong("fly.ñoolDowns"));
            p.closeInventory();
			
		}));
		
		Material helpId = Material.getMaterial(Main.getInstance().getConfig().getInt("fly.GUI.help.id"));
		int helpSlot = Main.getInstance().getConfig().getInt("fly.GUI.help.slot");
		String helpName = Main.getInstance().getConfig().getString("fly.GUI.help.name").replace("&", "§");
		List<String> helpLore = Main.getInstance().getConfig().getStringList("fly.GUI.help.lores");
		for (int l = 0; l < helpLore.size(); ++l) {
			helpLore.set(l, helpLore.get(l).replace("&", "§"));
		}

		this.gui.addItem(helpSlot, ItemUtil.createItemStack(helpId, helpName, helpLore), new GUI.InventoryAction(p -> {
			p.closeInventory();
			player.sendMessage(Util.getMessage("fly.GUI.help.url").replace("%prefix%", Util.getMessage("fly.msg.prefix")));
		}));
		
		Material subsId = Material.getMaterial(Main.getInstance().getConfig().getInt("fly.GUI.subs.id"));
		int subsSlot = Main.getInstance().getConfig().getInt("fly.GUI.subs.slot");
		String subsName = Main.getInstance().getConfig().getString("fly.GUI.subs.name").replace("&", "§");
		List<String> subsLore = Main.getInstance().getConfig().getStringList("fly.GUI.subs.lores");
		
		for (int l = 0; l < subsLore.size(); ++l) {
			subsLore.set(l, subsLore.get(l).replace("&", "§").replace("%timeSubs%", 
					timeLeft > 0 ? Util.secondsToDhms((int)timeLeft) : Util.getMessage("messages.timeSubsNull")
						));
		}

		this.gui.addItem(subsSlot, ItemUtil.createItemStack(subsId, subsName, subsLore), new GUI.InventoryAction(p -> {
			generate(player);
		}));
				
		for (int i = 28; i < 37; ++i) {
			gui.addItem(i, ItemUtil.createDataItemStack(Material.STAINED_GLASS_PANE, 7, " "),  new GUI.InventoryAction(p -> {
			}));
		}
		
		int index = 0;
		int[] history_slots = new int[9];
		for (int i = 37; i < 46; ++i) {
			history_slots[index] = i;
			++index;
		}

		index = 0;
		if (Main.getInstance().getConfig().getStringList("history.prizes") != null) {
			for (String history : Main.getInstance().getConfig().getStringList("history.prizes")) {

				String[] hist = history.split(";");

				List<String> historyLore = Main.getInstance().getConfig().getStringList("history.format.lore");
				for (int l = 0; l < historyLore.size(); ++l) {
					historyLore.set(l, historyLore.get(l).replace("&", "§").replace("%nameSubs%", hist[1])
							.replace("%timeSubs%", hist[3]).replace("%time%", hist[2]));
				}

				gui.addItem(history_slots[index],
						ItemUtil.createSkullNick(hist[0], Util.getMessage("history.format.name").replace("%player%", hist[0]), historyLore),
						new GUI.InventoryAction(p -> {
							generate(player);
						}));
				++index;
			}
		}
	}
}