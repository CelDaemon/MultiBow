package net.voidgroup.paper.test_plugin;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import org.bukkit.ServerTickManager;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TrailTNT implements Listener {
    private static final int EXPLOSION_DELAY = 30;
    private static final int DEPLOY_TIME = EXPLOSION_DELAY / 3;
    private final TestPlugin plugin;
    private final BukkitScheduler scheduler;
    private final ServerTickManager tickManager;

    private final Map<Entity, BukkitTask> tasks = new HashMap<>();
    public TrailTNT(TestPlugin plugin) {
        this.plugin = plugin;
        final var server = plugin.getServer();
        this.scheduler = server.getScheduler();
        this.tickManager = server.getServerTickManager();
    }
    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if(event.getEntityType() != EntityType.SKELETON) return;
        final var entity = (Skeleton) event.getEntity();
        entity.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, PotionEffect.INFINITE_DURATION, 255, true, false));
    }
    @EventHandler
    public void onArrowLaunch(ProjectileLaunchEvent event) {
        final var entity = event.getEntity();
        if(!(entity instanceof Arrow arrow) || plugin.isNotArrowType(arrow, "Trail Arrow")) return;
        final var counter = new AtomicInteger(EXPLOSION_DELAY);
        tasks.put(entity, scheduler.runTaskTimer(plugin, () -> {
            if(tickManager.isFrozen() || !entity.isValid() || !entity.hasLeftShooter()) return;
            final var count = counter.addAndGet(-1);
            if(count < DEPLOY_TIME) return;
            final var location = entity.getLocation();
            final var tnt = (TNTPrimed) location.getWorld().spawnEntity(location, EntityType.TNT);
            tnt.setFuseTicks(count);
            plugin.setPreventBlockBreak(tnt, true);
        }, 0, 1));
    }
    @EventHandler
    public void onEntityRemove(EntityRemoveFromWorldEvent event) {
        final var entity = event.getEntity();
        if(tasks.containsKey(entity)) tasks.remove(entity).cancel();
    }
    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        final var entity = event.getEntity();
        if(tasks.containsKey(entity)) tasks.remove(entity).cancel();
    }
}
