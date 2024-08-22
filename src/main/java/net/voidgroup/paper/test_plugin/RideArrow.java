package net.voidgroup.paper.test_plugin;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class RideArrow implements Listener {
    private final TestPlugin plugin;
    public RideArrow(final TestPlugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onProjectileLaunch(final ProjectileLaunchEvent event) {
        final var projectile = event.getEntity();
        if(!(projectile.getShooter() instanceof final LivingEntity shooter) || !(projectile instanceof Arrow arrow) || plugin.isNotArrowType(arrow, "Mountable Arrow")) return;
        projectile.addPassenger(shooter);
    }
    @EventHandler
    public void onProjectileHit(final ProjectileHitEvent event) {
        event.getEntity().eject();
    }
}
