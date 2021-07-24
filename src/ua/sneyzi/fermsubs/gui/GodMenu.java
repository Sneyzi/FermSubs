package ua.sneyzi.fermsubs.gui;

import org.bukkit.entity.*;
import org.bukkit.plugin.*;

import ua.sneyzi.fermsubs.Main;
import ua.sneyzi.fermsubs.User;
import ua.sneyzi.fermsubs.gui.api.GUI;
import ua.sneyzi.fermsubs.gui.api.ItemUtil;
import ua.sneyzi.fermsubs.utils.Util;

import java.util.List;

import org.bukkit.*;

public class GodMenu {
	private GUI gui;

	public GodMenu(final Player player) {
		this.gui = new GUI(Util.getMessage("god.GUI.headline"), 5);
		player.openInventory(this.gui.getInventory());
		Bukkit.getScheduler().runTaskAsynchronously((Plugin) Main.getInstance(), () -> this.generate(player));
	}

	public void generate(Player player) {
		String enabled = Main.getInstance().getConfig().getString("messages.working.enabled").replace("&", "§");
		String disabled = Main.getInstance().getConfig().getString("messages.working.disabled").replace("&", "§");
		long timeLeft = User.hasSubscription(player.getName().toLowerCase(), "god") - System.currentTimeMillis() / 1000;

		Material godId = Material.getMaterial(Main.getInstance().getConfig().getInt("god.GUI.god.id"));
		int godSlot = Main.getInstance().getConfig().getInt("god.GUI.god.slot");
		String godName = Main.getInstance().getConfig().getString("god.GUI.god.name").replace("&", "§");
		List<String> godLore = Main.getInstance().getConfig().getStringList("god.GUI.god.lores");
		for (int l = 0; l < godLore.size(); ++l) {
			godLore.set(l, godLore.get(l).replace("&", "§").replace("%working%", Main.getInstance().getEssentials().getUser(player).isGodModeEnabled() ? enabled : disabled));
		}

		this.gui.addItem(godSlot, ItemUtil.createItemStack(godId, godName, godLore), new GUI.InventoryAction(p -> {
			if (Main.getInstance().cooldown.cd.containsKey(player.getName().toLowerCase()+"god")) {
				p.sendMessage(Util.getMessage("god.msg.cooldown")
						.replace("%time%", Main.getInstance().cooldown.cd.get(player.getName().toLowerCase()+"god").toString())
						.replace("%prefix%", Util.getMessage("god.msg.prefix")));
				p.closeInventory();
				return;
			}
			
			if(User.hasSubscription(player.getName().toLowerCase(), "god") > 0) {
				if (Main.getInstance().getEssentials().getUser(player).isGodModeEnabled()) {
					Main.getInstance().getEssentials().getUser(player).setGodModeEnabled(false);
					player.sendMessage(Util.getMessage("god.msg.disabled").replace("%prefix%", Util.getMessage("god.msg.prefix")));
				} else {
					Main.getInstance().getEssentials().getUser(player).setGodModeEnabled(true);
					player.sendMessage(Util.getMessage("god.msg.enabled").replace("%prefix%", Util.getMessage("god.msg.prefix")));
				}
			} else {
				player.sendMessage(Util.getMessage("god.msg.buy").replace("%prefix%", Util.getMessage("god.msg.prefix")));
			}
            Main.getInstance().cooldown.cd.put(player.getName().toLowerCase()+"god", Main.getInstance().getConfig().getLong("god.ñoolDowns"));
			p.closeInventory();
		}));
		
		Material helpId = Material.getMaterial(Main.getInstance().getConfig().getInt("god.GUI.help.id"));
		int helpSlot = Main.getInstance().getConfig().getInt("god.GUI.help.slot");
		String helpName = Main.getInstance().getConfig().getString("god.GUI.help.name").replace("&", "§");
		List<String> helpLore = Main.getInstance().getConfig().getStringList("god.GUI.help.lores");
		for (int l = 0; l < helpLore.size(); ++l) {
			helpLore.set(l, helpLore.get(l).replace("&", "§"));
		}

		this.gui.addItem(helpSlot, ItemUtil.createItemStack(helpId, helpName, helpLore), new GUI.InventoryAction(p -> {
			p.closeInventory();
			player.sendMessage(Util.getMessage("god.GUI.help.url").replace("%prefix%", Util.getMessage("god.msg.prefix")));
		}));
		
		Material subsId = Material.getMaterial(Main.getInstance().getConfig().getInt("god.GUI.subs.id"));
		int subsSlot = Main.getInstance().getConfig().getInt("god.GUI.subs.slot");
		String subsName = Main.getInstance().getConfig().getString("god.GUI.subs.name").replace("&", "§");
		List<String> subsLore = Main.getInstance().getConfig().getStringList("god.GUI.subs.lores");
		
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