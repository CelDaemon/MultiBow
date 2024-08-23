package net.voidgroup.paper.multibow;

import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public class BowEventHandler implements Listener {
    private final MultiBowPlugin plugin;
    private final PluginManager manager;
    private final BowTypeManager bowTypeManager;
    public BowEventHandler(@NotNull final MultiBowPlugin plugin, @NotNull final PluginManager manager, @NotNull final BowTypeManager bowTypeManager) {
        this.plugin = plugin;
        this.manager = manager;
        this.bowTypeManager = bowTypeManager;
    }
    public void registerEvents() {
        manager.registerEvents(this, plugin);
    }

    @EventHandler
    public void onShootBow(@NotNull final EntityShootBowEvent event) {
        final var item = event.getBow();
        if(item == null) return;
        final var bow = bowTypeManager.getBowType(item);
        if(bow == null) return;
        bow.onShoot(event);
    }
    @EventHandler
    public void onProjectileHit(@NotNull final ProjectileHitEvent event) {
        if(!(event.getEntity() instanceof final Arrow arrow)) return;
        @SuppressWarnings("UnstableApiUsage")
        final var bowItem = arrow.getWeapon();
        if(bowItem == null) return;
        final var bow = bowTypeManager.getBowType(bowItem);
        if(bow == null) return;
        bow.onHit(event);
    }
}
