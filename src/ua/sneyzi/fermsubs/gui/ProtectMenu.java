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

public class ProtectMenu {
	private GUI gui;

	public ProtectMenu(final Player player) {
		this.gui = new GUI(Util.getMessage("protect.GUI.headline"), 5);
		player.openInventory(this.gui.getInventory());
		Bukkit.getScheduler().runTaskAsynchronously((Plugin) Main.getInstance(), () -> this.generate(player));
	}

	public void generate(Player player) {
		String enabled = Main.getInstance().getConfig().getString("messages.working.enabled").replace("&", "§");
		String disabled = Main.getInstance().getConfig().getString("messages.working.disabled").replace("&", "§");
		long timeLeft = User.hasSubscription(player.getName().toLowerCase(), "protect")
				- System.currentTimeMillis() / 1000;

		Material protectId = Material.getMaterial(Main.getInstance().getConfig().getInt("protect.GUI.protect.id"));
		int protectSlot = Main.getInstance().getConfig().getInt("protect.GUI.protect.slot");
		String protectName = Main.getInstance().getConfig().getString("protect.GUI.protect.name").replace("&", "§");
		List<String> protectLore = Main.getInstance().getConfig().getStringList("protect.GUI.protect.lores");
		for (int l = 0; l < protectLore.size(); ++l) {
			protectLore.set(l,
					protectLore.get(l).replace("&", "§").replace("%working%", timeLeft > 0 ? enabled : disabled));
		}

		this.gui.addItem(protectSlot, ItemUtil.createItemStack(protectId, protectName, protectLore),
				new GUI.InventoryAction(p -> {
					if (User.hasSubscription(player.getName().toLowerCase(), "protect") < 0) {
						player.sendMessage(Util.getMessage("protect.msg.buy").replace("%prefix%",
								Util.getMessage("protect.msg.prefix")));
					}
					p.closeInventory();
				}));

		Material helpId = Material.getMaterial(Main.getInstance().getConfig().getInt("protect.GUI.help.id"));
		int helpSlot = Main.getInstance().getConfig().getInt("protect.GUI.help.slot");
		String helpName = Main.getInstance().getConfig().getString("protect.GUI.help.name").replace("&", "§");
		List<String> helpLore = Main.getInstance().getConfig().getStringList("protect.GUI.help.lores");
		for (int l = 0; l < helpLore.size(); ++l) {
			helpLore.set(l, helpLore.get(l).replace("&", "§"));
		}

		this.gui.addItem(helpSlot, ItemUtil.createItemStack(helpId, helpName, helpLore), new GUI.InventoryAction(p -> {
			p.closeInventory();
			player.sendMessage(
					Util.getMessage("protect.GUI.help.url").replace("%prefix%", Util.getMessage("protect.msg.prefix")));
		}));

		Material subsId = Material.getMaterial(Main.getInstance().getConfig().getInt("protect.GUI.subs.id"));
		int subsSlot = Main.getInstance().getConfig().getInt("protect.GUI.subs.slot");
		String subsName = Main.getInstance().getConfig().getString("protect.GUI.subs.name").replace("&", "§");
		List<String> subsLore = Main.getInstance().getConfig().getStringList("protect.GUI.subs.lores");

		for (int l = 0; l < subsLore.size(); ++l) {
			subsLore.set(l, subsLore.get(l).replace("&", "§").replace("%timeSubs%",
					timeLeft > 0 ? Util.secondsToDhms((int) timeLeft) : Util.getMessage("messages.timeSubsNull")));
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