package net.voidgroup.paper.test_plugin;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.util.Vector;

public class SpreadTNT implements Listener {
    @EventHandler
    public void onArrowShoot(ProjectileLaunchEvent event) {
        var location = event.getLocation();
        for (int i = 0; i < 10; i++) {
            location.getWorld().spawnEntity(location, EntityType.TNT).setVelocity(Vector.getRandom().subtract(new Vector(-0.5f, -0.5f, -0.5f)).multiply(2));
        }
        event.setCancelled(true);
    }
}
