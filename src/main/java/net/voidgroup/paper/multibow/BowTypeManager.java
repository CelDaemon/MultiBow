package net.voidgroup.paper.multibow;

import net.voidgroup.paper.multibow.bows.*;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BowTypeManager implements Listener {
    private final NamespacedKey bowTypeKey;
    private final MultiBowPlugin plugin;
    private final Map<NamespacedKey, BowType> bowTypeRegistry = new HashMap<>();
    public BowTypeManager(@NotNull final MultiBowPlugin plugin) {
        this.bowTypeKey = new NamespacedKey(plugin, "bow_type");
        this.plugin = plugin;
    }
    public void registerBowType(@NotNull final BowType bowType) {
        bowTypeRegistry.putIfAbsent(bowType.getKey(), bowType);
    }
    public void registerBowTypes() {
        registerBowType(new TestBowType());
        registerBowType(new ExplosiveBowType());
        registerBowType(new AdminRemoveBowType());
        registerBowType(new AdminBanBowType());
        registerBowType(new DispersionBowType());
    }
    public @Nullable BowType getRegisteredBowType(@NotNull final NamespacedKey key) {
        return bowTypeRegistry.get(key);
    }
    public @Nullable BowType getItemBowType(@NotNull final ItemStack item) {
        final var bowString = item.getPersistentDataContainer().get(bowTypeKey, PersistentDataType.STRING);
        if(bowString == null) return null;
        final var bowKey = NamespacedKey.fromString(bowString, plugin);
        if(bowKey == null) return null;
        return getRegisteredBowType(bowKey);
    }
    public void convertBow(@NotNull final ItemStack item, @NotNull final BowType bowType) {
        resetBow(item);
        final var meta = item.getItemMeta();
        final var dataContainer = meta.getPersistentDataContainer();
        dataContainer.set(bowTypeKey, PersistentDataType.STRING, bowType.getKey().asString());
        bowType.convert(meta);
        item.setItemMeta(meta);
    }
    public void resetBow(@NotNull final ItemStack item) {
        final var meta = item.getItemMeta();
        final var dataContainer = meta.getPersistentDataContainer();
        final var bowType = getItemBowType(item);
        if(bowType != null) bowType.reset(meta);
        dataContainer.remove(bowTypeKey);
        item.setItemMeta(meta);
    }
    public @NotNull Map<NamespacedKey, BowType> getRegisteredBowTypes() {
        return Collections.unmodifiableMap(bowTypeRegistry);
    }
}
