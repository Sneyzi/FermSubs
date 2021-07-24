package ua.sneyzi.fermsubs.listeners;

import java.util.Arrays;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import ua.sneyzi.fermsubs.User;
import ua.sneyzi.fermsubs.utils.Util;

public class EventListener implements Listener {

	private Plugin plugin;

	public EventListener(Plugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void preprocessCommand(PlayerCommandPreprocessEvent e) {
		List<String> blockedCommands = Arrays.asList("ban", "kick", "mute");

		String command = e.getMessage().substring(1).toLowerCase();
		String[] args = command.split(" ");

		if (args.length >= 2) {
			for (String block : blockedCommands) {
				if (block.equals(args[0])) {
					if (User.hasSubscription(args[1], "protect") > 0) {
						e.setCancelled(true);
						e.getPlayer().sendMessage(Util.getMessage("protect.msg.protected").replace("%player%", e.getPlayer().getName()));
					}
				}
			}
		}
	}
}