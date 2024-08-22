package net.voidgroup.paper.test_plugin;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin implements Listener {
    private final NamespacedKey PREVENT_BLOCK_EXPLODE = new NamespacedKey(this, "prevent_block_explode");
    private final PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();

    @Override
    public void onEnable() {
        Bukkit.getScheduler().cancelTasks(this);
        final var pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(this, this);
        pluginManager.registerEvents(new ShootTNT(this), this);
        pluginManager.registerEvents(new RideArrow(this), this);
        pluginManager.registerEvents(new ReturnToSender(this), this);
        pluginManager.registerEvents(new ArrowTNTIgnite(this), this);
        pluginManager.registerEvents(new TrailTNT(this), this);

    }
    public void setPreventBlockBreak(final TNTPrimed tnt, final boolean state) {
        tnt.getPersistentDataContainer().set(PREVENT_BLOCK_EXPLODE, PersistentDataType.BOOLEAN, state);
    }
    public boolean shouldPreventBlockBreak(final TNTPrimed tnt) {
        return Boolean.TRUE.equals(tnt.getPersistentDataContainer().get(PREVENT_BLOCK_EXPLODE, PersistentDataType.BOOLEAN));
    }
    public boolean isNotArrowType(Arrow arrow, String name) {
        var displayName = (TextComponent) arrow.getItemStack().getItemMeta().displayName();
        if(displayName == null) return true;
        return !serializer.serialize(displayName).equalsIgnoreCase(name);
    }
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if(!(event.getEntity() instanceof TNTPrimed tnt) || !shouldPreventBlockBreak(tnt)) return;
        event.blockList().clear();
    }
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player player) || !(event.getEntity() instanceof LivingEntity entity)) return;
        var damage = event.getFinalDamage();
        player.sendMessage("Health: " + Math.round(Math.max((entity.getHealth() - damage), 0)) + " (-" + Math.round(damage) + ")");
    }
}
