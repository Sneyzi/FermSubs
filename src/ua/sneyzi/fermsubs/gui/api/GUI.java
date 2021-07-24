package ua.sneyzi.fermsubs.gui.api;

import java.util.*;
import org.bukkit.inventory.*;
import org.bukkit.event.inventory.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

import org.bukkit.plugin.*;

import ua.sneyzi.fermsubs.Main;

import org.bukkit.*;

public class GUI implements InventoryHolder
{
    private Inventory inv;
    private HashMap<Integer, InventoryAction> runnableMap;
    
    public GUI(String title, int rows) {
        this.runnableMap = new HashMap<Integer, InventoryAction>();
        this.inv = Bukkit.createInventory((InventoryHolder)this, 9 * rows, title);
    }
    
    public void clear() {
        this.inv.clear();
        this.runnableMap.clear();
    }
    
    public Inventory getInventory() {
        return this.inv;
    }
    
    public void addItem(int slot, ItemStack item, InventoryAction runnable) {
        this.inv.setItem(slot - 1, item);
        this.runnableMap.put(slot - 1, runnable);
    }
    
    public void removeItem(int slot) {
        this.inv.clear(slot);
        this.runnableMap.remove(slot);
    }
    
    public void onInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (e.getClickedInventory() == null || e.getClickedInventory().getHolder() != this) {
            return;
        }
        InventoryAction action = this.runnableMap.get(e.getSlot());
        if (action == null) {
            return;
        }
        switch (e.getClick()) {
            case LEFT: {
                action.click((Player)e.getWhoClicked());
                break;
            }
            case RIGHT: {
                action.rightClick((Player)e.getWhoClicked());
                break;
            }
            case SHIFT_LEFT: {
                action.shiftClick((Player)e.getWhoClicked());
                break;
            }
        }
    }
    
    static {
        Bukkit.getPluginManager().registerEvents((Listener)new Listener() {
            @EventHandler
            public void onClick(InventoryClickEvent e) {
                if (e.getInventory().getHolder() instanceof GUI) {
                    ((GUI)e.getInventory().getHolder()).onInventoryClick(e);
                }
            }
        }, (Plugin)Main.getInstance());
    }
    
    public static class InventoryAction
    {
        private PlayerRunnable click;
        private PlayerRunnable rightClick;
        private PlayerRunnable shiftClick;
        
        public InventoryAction(PlayerRunnable click, Sound sound) {
            this(click, null, null);
        }
        
        public InventoryAction(PlayerRunnable click) {
            this(click, null, null);
        }
        
        public InventoryAction(PlayerRunnable click, PlayerRunnable rightClick, PlayerRunnable shiftClick) {
            this.click = click;
            this.rightClick = rightClick;
            this.shiftClick = shiftClick;
        }
        
        public void click(Player p) {
            this.click.onClick(p);
        }
        
        public void rightClick(Player p) {
            if (this.rightClick != null) {
                this.rightClick.onClick(p);
            }
        }
        
        public void shiftClick(Player p) {
            if (this.shiftClick != null) {
                this.shiftClick.onClick(p);
            }
        }
        
        public interface PlayerRunnable
        {
            void onClick(Player p0);
        }
    }
}
