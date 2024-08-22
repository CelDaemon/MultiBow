package net.voidgroup.paper.test_plugin;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class BurstTNT implements Listener {
    @EventHandler
    public void onArrowShoot(ProjectileLaunchEvent event) {
        var entity = event.getEntity();
        var velocity = entity.getVelocity();
        var location = event.getLocation();
        for (int i = 0; i < 10; i++) {
            var velocity2 = velocity.normalize().multiply(i / 10f + 1.2f);
            location.getWorld().spawnEntity(location, EntityType.TNT).setVelocity(velocity2);
        }

        event.setCancelled(true);
    }
}
