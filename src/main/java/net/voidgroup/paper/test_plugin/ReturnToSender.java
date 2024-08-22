package net.voidgroup.paper.test_plugin;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.atomic.AtomicInteger;

public class ReturnToSender implements Listener {
    private static final int RETURN_DELAY = 10;
    private final TestPlugin plugin;
    private final BukkitScheduler scheduler;

    public ReturnToSender(TestPlugin plugin) {
        this.plugin = plugin;
        final var server = plugin.getServer();
        scheduler = server.getScheduler();
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        final var projectile = event.getEntity();
        if(!(projectile instanceof Arrow arrow) || plugin.isNotArrowType(arrow, "Homing Arrow")) return;
        if(!(projectile.getShooter() instanceof LivingEntity shooter)) return;
        final var counter = new AtomicInteger(RETURN_DELAY);
        final var task = scheduler.runTaskTimer(plugin, () -> {
            if(!projectile.isValid() || !projectile.hasLeftShooter()) return;
            final var count = counter.addAndGet(-1);
            plugin.getLogger().info("Count: " + count);
            if(count > 0) return;
            projectile.setVelocity(shooter.getLocation().toVector().subtract(projectile.getLocation().toVector()).normalize().multiply(1));
        }, 0, 1);
        projectile.setMetadata("task", new FixedMetadataValue(plugin, task));
    }
    @EventHandler
    public void onEntityRemove(EntityRemoveFromWorldEvent event) {
        if(!(event.getEntity() instanceof Projectile projectile)) return;
        final var metadata = projectile.getMetadata("task");
        if(metadata.isEmpty()) return;
        final var task = (BukkitTask) metadata.getFirst().value();
        if(task == null) return;
        task.cancel();
    }
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        final var projectile = event.getEntity();
        final var metadata = projectile.getMetadata("task");
        if(metadata.isEmpty()) return;
        final var task = (BukkitTask) metadata.getFirst().value();
        if(task == null) return;
        task.cancel();
    }
}
