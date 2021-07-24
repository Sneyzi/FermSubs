package ua.sneyzi.fermsubs.commands;

import java.util.Iterator;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.Lists;

import net.md_5.bungee.api.ChatColor;
import ua.sneyzi.fermsubs.User;
import ua.sneyzi.fermsubs.utils.Util;

public class SubsCommand implements CommandExecutor {

	private Plugin plugin;

	public SubsCommand(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("fermsubs.admin")) {
			sender.sendMessage(Util.getMessage("noPerm").replace("%prefix%", Util.getMessage("prefix")));
			return true;
		}
		if (args.length == 0) {
			List<String> helpadmin = plugin.getConfig().getStringList("messages.help");
			for (String s : helpadmin) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s).replace("%prefix%", Util.getMessage("messages.prefix")));
			}
			return true;
		}
			
		if (args[0].equals("add")) {
			if (args.length != 4) {
				List<String> helpadmin = plugin.getConfig().getStringList("messages.help");
				for (String s : helpadmin) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s).replace("%prefix%", Util.getMessage("messages.prefix")));
				}
				return true;
			}
			
			if (!Util.checkFormat(args[3])) {
				sender.sendMessage(Util.getMessage("messages.notTime").replace("%time%", args[3]));
				return true;
			}
			
			if (args[1].equals("fly")) {
				User.addSubscription(args[2].toLowerCase(), "fly", Util.parseTime(args[3]));
				sender.sendMessage(Util.getMessage("messages.add").replace("%prefix%", Util.getMessage("messages.prefix")).replace("%timeSubs%", Util.formatTime(args[3])).replace("%nameSubs%", Util.getMessage("messages.nameSubs.fly")).replace("%player%", args[2]));
			}
				
			if (args[1].equals("god")) {
				User.addSubscription(args[2].toLowerCase(), "god", Util.parseTime(args[3]));
				sender.sendMessage(Util.getMessage("messages.add").replace("%prefix%", Util.getMessage("messages.prefix")).replace("%timeSubs%", args[3]).replace("%nameSubs%", Util.getMessage("messages.nameSubs.god")).replace("%player%", args[2]));
			}
			
			if (args[1].equals("protect")) {
				User.addSubscription(args[2].toLowerCase(), "protect", Util.parseTime(args[3]));
				sender.sendMessage(Util.getMessage("messages.add").replace("%prefix%", Util.getMessage("messages.prefix")).replace("%timeSubs%", args[3]).replace("%nameSubs%", Util.getMessage("messages.nameSubs.protect")).replace("%player%", args[2]));	
			}
			
			
			List<String> last = Lists.newArrayList();
			last.add(args[2] + ";" + args[1] + ";" + Util.getDate() + ";" + Util.formatTime(args[3]));
			Iterator<?> iterator = plugin.getConfig().getStringList("history.prizes").iterator();

			while (iterator.hasNext()) {
				String s = (String) iterator.next();
				last.add(s);
				if (last.size() == 9) {
					break;
				}
			}

            plugin.getConfig().set("history.prizes", last);
            plugin.saveConfig();
            return true;
		}
		
		if (args[0].equals("take")) {
			if (args.length != 4) {
				List<String> helpadmin = plugin.getConfig().getStringList("messages.help");
				for (String s : helpadmin) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s).replace("%prefix%", Util.getMessage("messages.prefix")));
				}
				return true;
			}
			
			if (!Util.checkFormat(args[3])) {
				sender.sendMessage(Util.getMessage("messages.notTime").replace("%time%", args[3]));
				return true;
			}
			
			if (args[1].equals("fly")) {
				User.removeSubscription(args[2].toLowerCase(), "fly", Util.parseTime(args[3]));
				sender.sendMessage(Util.getMessage("messages.take").replace("%prefix%", Util.getMessage("messages.prefix")).replace("%timeSubs%", Util.formatTime(args[3])).replace("%nameSubs%", Util.getMessage("messages.nameSubs.fly")).replace("%player%", args[2]));
				return true;
			}
				
			if (args[1].equals("god")) {
				User.removeSubscription(args[2].toLowerCase(), "god", Util.parseTime(args[3]));
				sender.sendMessage(Util.getMessage("messages.take").replace("%prefix%", Util.getMessage("messages.prefix")).replace("%timeSubs%", Util.formatTime(args[3])).replace("%nameSubs%", Util.getMessage("messages.nameSubs.god")).replace("%player%", args[2]));
				return true;
			}
			
			if (args[1].equals("protect")) {
				User.removeSubscription(args[2].toLowerCase(), "protect", Util.parseTime(args[3]));
				sender.sendMessage(Util.getMessage("messages.take").replace("%prefix%", Util.getMessage("messages.prefix")).replace("%timeSubs%", Util.formatTime(args[3])).replace("%nameSubs%", Util.getMessage("messages.nameSubs.protect")).replace("%player%", args[2]));
				return true;
			}
		}
		
		if (args[0].equals("reload")) {
			plugin.reloadConfig();
			sender.sendMessage(Util.getMessage("messages.reload").replace("%prefix%", Util.getMessage("messages.prefix")));
			return true;
		}
		
		List<String> helpadmin = plugin.getConfig().getStringList("messages.help");
		for (String s : helpadmin) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s).replace("%prefix%", Util.getMessage("messages.prefix")));
		}
		
		return true;
	}
}