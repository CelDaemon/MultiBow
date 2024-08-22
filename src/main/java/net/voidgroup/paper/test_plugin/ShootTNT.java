package net.voidgroup.paper.test_plugin;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class ShootTNT implements Listener {
    private final TestPlugin plugin;
    public ShootTNT(TestPlugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onProjectile(final ProjectileLaunchEvent event) {
        if(!(event.getEntity() instanceof Arrow arrow) || plugin.isNotArrowType(arrow, "TNT Cannon")) return;
        event.setCancelled(true);
        final var location = event.getLocation();
        final var tnt = (TNTPrimed) location.getWorld().spawnEntity(location, EntityType.TNT);
        tnt.setVelocity(event.getEntity().getVelocity());
        tnt.setPersistent(false);
        plugin.setPreventBlockBreak(tnt, true);
    }
}
