package net.voidgroup.paper.multibow;

import net.voidgroup.paper.multibow.bows.BowType;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BowTypeManager implements Listener {
    private final NamespacedKey bowTypeKey;
    private final MultiBowPlugin plugin;
    private final Map<NamespacedKey, BowType> bowTypeRegistry = new HashMap<>();
    public BowTypeManager(final MultiBowPlugin plugin) {
        this.bowTypeKey = new NamespacedKey(plugin, "bow_type");
        this.plugin = plugin;
    }
    public void registerBowType(final BowType bowType) {
        bowTypeRegistry.putIfAbsent(bowType.getKey(), bowType);
    }
    public @Nullable BowType getBowType(final NamespacedKey key) {
        return bowTypeRegistry.get(key);
    }
    public @Nullable BowType getBowType(final PersistentDataContainer dataContainer) {
        final String bowString = dataContainer.get(bowTypeKey, PersistentDataType.STRING);
        if(bowString == null) return null;
        return getBowType(NamespacedKey.fromString(bowString, plugin));
    }
    public void convertBow(final ItemStack item, final BowType bowType) {
        final var meta = item.getItemMeta();
        final var dataContainer = meta.getPersistentDataContainer();
        final var oldBowType = getBowType(dataContainer);
        if(oldBowType != null) oldBowType.reset(meta);
        dataContainer.set(bowTypeKey, PersistentDataType.STRING, bowType.getKey().asString());
        bowType.convert(meta);
        item.setItemMeta(meta);
    }
    public void resetBow(final ItemStack item) {
        final var meta = item.getItemMeta();
        final var dataContainer = meta.getPersistentDataContainer();
        final var bowType = getBowType(dataContainer);
        if(bowType != null) bowType.reset(meta);
        dataContainer.remove(bowTypeKey);
        item.setItemMeta(meta);
    }
    public Map<NamespacedKey, BowType> getRegisteredBowTypes() {
        return Collections.unmodifiableMap(bowTypeRegistry);
    }
}
