package ua.sneyzi.fermsubs.gui.api;

import org.bukkit.inventory.meta.*;
import org.bukkit.*;
import java.util.*;
import com.mojang.authlib.*;
import org.apache.commons.codec.binary.Base64;

import com.mojang.authlib.properties.*;

import java.lang.reflect.*;
import org.bukkit.inventory.*;
import org.bukkit.enchantments.*;

public class ItemUtil {
	public static ItemStack createSkullNick(String nickname) {
		try {
			ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			SkullMeta skull = (SkullMeta) item.getItemMeta();
			skull.setOwner(nickname);
			item.setItemMeta((ItemMeta) skull);
			return item;
		} catch (Exception e) {
			List<String> stackTrace = new ArrayList<String>();
			StackTraceElement[] stackTrace2;
			for (int length = (stackTrace2 = e.getStackTrace()).length, i = 0; i < length; ++i) {
				StackTraceElement element = stackTrace2[i];
				stackTrace.add(ChatColor.YELLOW + element.toString());
			}
			return createItemStack(Material.BARRIER, "§cТы дура или da?",
					(String[]) stackTrace.toArray(new String[stackTrace.size()]));
		}
	}

	public static ItemStack createSkullNick(String nickname, String displayName, String... lore) {
		try {
			ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			SkullMeta skull = (SkullMeta) item.getItemMeta();
			skull.setOwner(nickname);
			skull.setDisplayName(displayName);
			skull.setLore((List) Arrays.asList(lore));
			item.setItemMeta((ItemMeta) skull);
			return item;
		} catch (Exception e) {
			List<String> stackTrace = new ArrayList<String>();
			StackTraceElement[] stackTrace2;
			for (int length = (stackTrace2 = e.getStackTrace()).length, i = 0; i < length; ++i) {
				StackTraceElement element = stackTrace2[i];
				stackTrace.add(ChatColor.YELLOW + element.toString());
			}
			return createItemStack(Material.BARRIER, "§cТы дура или da?",
					(String[]) stackTrace.toArray(new String[stackTrace.size()]));
		}
	}

	public static ItemStack createSkullTexture(String url, String displayName, String... lore) {
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		if (url == null || url.isEmpty()) {
			return skull;
		}
		SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), (String) null);
		byte[] encodedData = Base64.encodeBase64(String
				.format("{textures:{SKIN:{url:\"%s\"}}}", "http://textures.minecraft.net/texture/" + url).getBytes());
		profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
		Field profileField = null;
		try {
			profileField = skullMeta.getClass().getDeclaredField("profile");
		} catch (NoSuchFieldException | SecurityException ex) {
			ex.printStackTrace();
		}
		profileField.setAccessible(true);
		try {
			profileField.set(skullMeta, profile);
		} catch (IllegalArgumentException | IllegalAccessException ex) {
			ex.printStackTrace();
		}
		skullMeta.setDisplayName(displayName);
		skullMeta.setLore((List) Arrays.asList(lore));
		skull.setItemMeta((ItemMeta) skullMeta);
		return skull;
	}

	public static ItemStack createSkullTexture(String url, String displayName, List<String> lores) {
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		if (url == null || url.isEmpty()) {
			return skull;
		}
		SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), (String) null);
		byte[] encodedData = Base64.encodeBase64(String
				.format("{textures:{SKIN:{url:\"%s\"}}}", "http://textures.minecraft.net/texture/" + url).getBytes());
		profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
		Field profileField = null;
		try {
			profileField = skullMeta.getClass().getDeclaredField("profile");
		} catch (NoSuchFieldException | SecurityException ex) {
			ex.printStackTrace();
		}
		profileField.setAccessible(true);
		try {
			profileField.set(skullMeta, profile);
		} catch (IllegalArgumentException | IllegalAccessException ex) {
			ex.printStackTrace();
		}

		skullMeta.setDisplayName(displayName);
		skullMeta.setLore((List) lores);
		skull.setItemMeta((ItemMeta) skullMeta);
		return skull;
	}

	public static ItemStack createItemStack(Material material, int data, String displayName,
			String... lore) {
		try {
			ItemStack item = new ItemStack(material, 1, (short) (byte) data);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(displayName);
			meta.setLore((List) Arrays.asList(lore));
			item.setItemMeta(meta);
			return item;
		} catch (Exception e) {
			List<String> stackTrace = new ArrayList<String>();
			StackTraceElement[] stackTrace2;
			for (int length = (stackTrace2 = e.getStackTrace()).length, i = 0; i < length; ++i) {
				StackTraceElement element = stackTrace2[i];
				stackTrace.add(ChatColor.YELLOW + element.toString());
			}
			return createItemStack(Material.BARRIER, "§cТы дура или da?",
					(String[]) stackTrace.toArray(new String[stackTrace.size()]));
		}
	}

	public static ItemStack createItemStack(Material material, int data, String displayName,
			List<String> lores) {
		try {
			ItemStack item = new ItemStack(material, 1, (short) (byte) data);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(displayName);
			meta.setLore((List) lores);
			meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_POTION_EFFECTS });
			meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
			meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DESTROYS });
			meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
			meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_PLACED_ON });
			meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_UNBREAKABLE });
			item.setItemMeta(meta);
			return item;
		} catch (Exception e) {
			List<String> stackTrace = new ArrayList<String>();
			StackTraceElement[] stackTrace2;
			for (int length = (stackTrace2 = e.getStackTrace()).length, i = 0; i < length; ++i) {
				StackTraceElement element = stackTrace2[i];
				stackTrace.add(ChatColor.YELLOW + element.toString());
			}
			return createItemStack(Material.BARRIER, "§cТы дура или da?",
					(String[]) stackTrace.toArray(new String[stackTrace.size()]));
		}
	}

	public static ItemStack createItemStack(Material material, String displayName, String... lore) {
		try {
			ItemStack item = new ItemStack(material, 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(displayName);
			meta.setLore((List) Arrays.asList(lore));
			meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_POTION_EFFECTS });
			meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
			meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DESTROYS });
			meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
			meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_PLACED_ON });
			meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_UNBREAKABLE });
			item.setItemMeta(meta);
			return item;
		} catch (Exception e) {
			List<String> stackTrace = new ArrayList<String>();
			StackTraceElement[] stackTrace2;
			for (int length = (stackTrace2 = e.getStackTrace()).length, i = 0; i < length; ++i) {
				StackTraceElement element = stackTrace2[i];
				stackTrace.add(ChatColor.YELLOW + element.toString());
			}
			return createItemStack(Material.BARRIER, "§cТы дура или da?",
					(String[]) stackTrace.toArray(new String[stackTrace.size()]));
		}
	}
	
	public static ItemStack createItemStack(Material material, String displayName, List<String> lore) {
		try {
			ItemStack item = new ItemStack(material, 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(displayName);
			meta.setLore(lore);
			meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_POTION_EFFECTS });
			meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
			meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DESTROYS });
			meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
			meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_PLACED_ON });
			meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_UNBREAKABLE });
			item.setItemMeta(meta);
			return item;
		} catch (Exception e) {
			List<String> stackTrace = new ArrayList<String>();
			StackTraceElement[] stackTrace2;
			for (int length = (stackTrace2 = e.getStackTrace()).length, i = 0; i < length; ++i) {
				StackTraceElement element = stackTrace2[i];
				stackTrace.add(ChatColor.YELLOW + element.toString());
			}
			return createItemStack(Material.BARRIER, "§cТы дура или da?",
					(String[]) stackTrace.toArray(new String[stackTrace.size()]));
		}
	}
	
	public static ItemStack createSkullNick(String nick, String displayName, List<String> lores) {
		try {
			ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			SkullMeta meta = (SkullMeta) item.getItemMeta();
			meta.setDisplayName(displayName);
			meta.setLore((List) lores);
			meta.setOwner(nick);
			item.setItemMeta((ItemMeta) meta);
			return item;
		} catch (Exception e) {
			List<String> stackTrace = new ArrayList<String>();
			StackTraceElement[] stackTrace2;
			for (int length = (stackTrace2 = e.getStackTrace()).length, i = 0; i < length; ++i) {
				StackTraceElement element = stackTrace2[i];
				stackTrace.add(ChatColor.YELLOW + element.toString());
			}
			return createItemStack(Material.BARRIER, "§cТы дура или da?",
					(String[]) stackTrace.toArray(new String[stackTrace.size()]));
		}
	}

	public static ItemStack createDataItemStack(Material material, int data, String displayName) {
		try {
			ItemStack item = new ItemStack(material, 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(displayName);
			meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_POTION_EFFECTS });
			meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
			meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DESTROYS });
			meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
			meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_PLACED_ON });
			meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_UNBREAKABLE });
			item.setItemMeta(meta);
			item.setDurability((short) data);
			return item;
		} catch (Exception e) {
			List<String> stackTrace = new ArrayList<String>();
			StackTraceElement[] stackTrace2;
			for (int length = (stackTrace2 = e.getStackTrace()).length, i = 0; i < length; ++i) {
				StackTraceElement element = stackTrace2[i];
				stackTrace.add(ChatColor.YELLOW + element.toString());
			}
			return createItemStack(Material.BARRIER, "§cТы дура или da?",
					(String[]) stackTrace.toArray(new String[stackTrace.size()]));
		}
	}	
	
	public static ItemStack createItemStack(Material material) {
		try {
			ItemStack item = new ItemStack(material, 1);
			return item;
		} catch (Exception e) {
			List<String> stackTrace = new ArrayList<String>();
			StackTraceElement[] stackTrace2;
			for (int length = (stackTrace2 = e.getStackTrace()).length, i = 0; i < length; ++i) {
				StackTraceElement element = stackTrace2[i];
				stackTrace.add(ChatColor.YELLOW + element.toString());
			}
			return createItemStack(Material.BARRIER, "§cТы дура или da?",
					(String[]) stackTrace.toArray(new String[stackTrace.size()]));
		}
	}

	public static GameProfile setGameProfileTexture(GameProfile p, String texture) {
		p.getProperties().put("textures", new Property("textures", texture));
		return p;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private ItemStack itemStack;
		private List<String> lores;

		public Builder() {
			this.itemStack = new ItemStack(Material.BARRIER, 1);
			ItemMeta meta = this.itemStack.getItemMeta();
			meta.setDisplayName("§cПредмет не создан!");
			this.itemStack.setItemMeta(meta);
			this.lores = new ArrayList<String>();
		}

		public Builder setMaterial(Material material) {
			this.itemStack.setType(material);
			return this;
		}

		public Builder setAmount(int i) {
			this.itemStack.setAmount(i);
			return this;
		}

		public Builder addEnchantment(Enchantment enchantment, int i) {
			if (this.itemStack.getType() != Material.SKULL_ITEM || this.itemStack.getType() != Material.SKULL) {
				ItemMeta meta = this.itemStack.getItemMeta();
				meta.addEnchant(enchantment, i, true);
				this.itemStack.setItemMeta(meta);
			} else {
				SkullMeta meta2 = (SkullMeta) this.itemStack.getItemMeta();
				meta2.addEnchant(enchantment, i, true);
				this.itemStack.setItemMeta((ItemMeta) meta2);
			}
			return this;
		}

		public Builder setDisplayName(String s) {
			if (this.itemStack.getType() != Material.SKULL_ITEM || this.itemStack.getType() != Material.SKULL) {
				ItemMeta meta = this.itemStack.getItemMeta();
				meta.setDisplayName(s.replace("&", "§"));
				this.itemStack.setItemMeta(meta);
			} else {
				SkullMeta meta2 = (SkullMeta) this.itemStack.getItemMeta();
				meta2.setDisplayName(s.replace("&", "§"));
				this.itemStack.setItemMeta((ItemMeta) meta2);
			}
			return this;
		}

		public Builder addLore(String lore) {
			this.lores.add(lore.replace("&", "§"));
			if (this.itemStack.getType() != Material.SKULL_ITEM || this.itemStack.getType() != Material.SKULL) {
				ItemMeta meta = this.itemStack.getItemMeta();
				meta.setLore((List) this.lores);
				this.itemStack.setItemMeta(meta);
			} else {
				SkullMeta meta2 = (SkullMeta) this.itemStack.getItemMeta();
				meta2.setLore((List) this.lores);
				this.itemStack.setItemMeta((ItemMeta) meta2);
			}
			return this;
		}

		public Builder setLores(List<String> lores) {
			if (this.itemStack.getType() != Material.SKULL_ITEM || this.itemStack.getType() != Material.SKULL) {
				ItemMeta meta = this.itemStack.getItemMeta();
				meta.setLore((List) lores);
				this.itemStack.setItemMeta(meta);
			} else {
				SkullMeta meta2 = (SkullMeta) this.itemStack.getItemMeta();
				meta2.setLore((List) lores);
				this.itemStack.setItemMeta((ItemMeta) meta2);
			}
			return this;
		}

		public Builder setLores(String... lores) {
			for (String s : lores) {
				this.lores.add(s.replace("&", "§"));
			}
			if (this.itemStack.getType() != Material.SKULL_ITEM || this.itemStack.getType() != Material.SKULL) {
				ItemMeta meta = this.itemStack.getItemMeta();
				meta.setLore((List) this.lores);
				this.itemStack.setItemMeta(meta);
			} else {
				SkullMeta meta2 = (SkullMeta) this.itemStack.getItemMeta();
				meta2.setLore((List) this.lores);
				this.itemStack.setItemMeta((ItemMeta) meta2);
			}
			return this;
		}

		public Builder setDurability(int i) {
			this.itemStack.setDurability((short) (byte) i);
			return this;
		}

		public Builder setSkullOwner(String skullOwner) {
			SkullMeta meta = (SkullMeta) this.itemStack.getItemMeta();
			meta.setOwner(skullOwner);
			this.itemStack.setItemMeta((ItemMeta) meta);
			return this;
		}

		public ItemStack build() {
			try {
				return this.itemStack;
			} catch (Exception e) {
				List<String> stackTrace = new ArrayList<String>();
				StackTraceElement[] stackTrace2;
				for (int length = (stackTrace2 = e.getStackTrace()).length, i = 0; i < length; ++i) {
					StackTraceElement element = stackTrace2[i];
					stackTrace.add(ChatColor.YELLOW + element.toString());
				}
				return ItemUtil.createItemStack(Material.BARRIER, "§cТы дура или da?",
						(String[]) stackTrace.toArray(new String[stackTrace.size()]));
			}
		}
	}
}
