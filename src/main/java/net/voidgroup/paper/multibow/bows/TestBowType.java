package net.voidgroup.paper.multibow.bows;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.voidgroup.paper.multibow.Util.translatable;

public class TestBowType extends BowType {
    public TestBowType() {
        super(new NamespacedKey("multibow", "test"), new Permission("multibow.bowtype.test"));
    }

    @Override
    public @NotNull Component getName() {
        return translatable("multibow.bowtype.test.name");
    }

    @Override
    public @NotNull List<Component> getLore() {
        return List.of(
                translatable("multibow.bowtype.test.lore.0", Style.style(NamedTextColor.DARK_PURPLE))
        );
    }

    @Override
    public @NotNull ItemRarity getRarity() {
        return ItemRarity.EPIC;
    }

    @Override
    public boolean destroyOnHit() {
        return true;
    }

    @Override
    public void onHit(@NotNull ProjectileHitEvent event) {
        super.onHit(event);
        final var hitEntity = event.getHitEntity();
        if(hitEntity == null ||
                !(event.getEntity().getShooter() instanceof final Entity shooter) ||
                hitEntity == shooter) return;
        hitEntity.addPassenger(shooter);
    }
}
