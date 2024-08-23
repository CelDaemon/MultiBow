package net.voidgroup.paper.multibow.bows;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.NamespacedKey;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public abstract class BowType {
    private final NamespacedKey key;
    protected BowType(@NotNull final NamespacedKey key) {
        this.key = key;
    }
    public void convert(@NotNull final ItemMeta meta) {
        meta.itemName(GlobalTranslator.render(getName(), Locale.US));
        meta.lore(getLore().stream().map(x -> GlobalTranslator.render(x, Locale.US)).toList());
        meta.setRarity(getRarity());
    }
    public void reset(@NotNull final ItemMeta meta) {
        meta.itemName(null);
        meta.lore(null);
        meta.setRarity(null);
    }
    public @NotNull NamespacedKey getKey() {
        return key;
    }
    public abstract @NotNull Component getName();
    public @NotNull List<Component> getLore() {
        return List.of();
    }
    public @NotNull ItemRarity getRarity() {
        return ItemRarity.UNCOMMON;
    }
    public boolean destroyOnHit() {
        return false;
    }
    public void onShoot(@NotNull final EntityShootBowEvent event) {}
    public void onHit(@NotNull final ProjectileHitEvent event) {
        if(destroyOnHit()) event.getEntity().remove();
    }
}
