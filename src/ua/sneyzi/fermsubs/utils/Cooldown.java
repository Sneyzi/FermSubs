package ua.sneyzi.fermsubs.utils;

import org.bukkit.scheduler.BukkitRunnable;


import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Cooldown extends BukkitRunnable {
    //public HashMap<UUID, Long> cd = new HashMap<>();
    public static ConcurrentHashMap<String, Long> cd = new ConcurrentHashMap<String, Long>();

    @Override
    public void run() {
        for(String uuid : cd.keySet()) {
            long time = cd.get(uuid)-1;

            if(time > 0) {
                cd.put(uuid, time);
            } else {
                cd.remove(uuid);
            }
        }
    }
}
