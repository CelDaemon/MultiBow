package net.voidgroup.paper.test_plugin;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ArrowTNTIgnite implements Listener {
    private final TestPlugin plugin;
    public ArrowTNTIgnite(TestPlugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        var block = event.getHitBlock();
        if(block == null || block.getType() != Material.TNT) return;
        block.setType(Material.AIR);
        final var tnt = (TNTPrimed) block.getWorld().spawnEntity(block.getLocation().toCenterLocation(), EntityType.TNT);
        tnt.setVelocity(event.getEntity().getVelocity());
        plugin.setPreventBlockBreak(tnt, true);
        event.getEntity().remove();
    }
}
