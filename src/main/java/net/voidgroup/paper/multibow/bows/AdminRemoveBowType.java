package net.voidgroup.paper.multibow.bows;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.voidgroup.paper.multibow.Util.translatable;
public class AdminRemoveBowType extends BowType {

    public AdminRemoveBowType(@NotNull NamespacedKey key) {
        super(key);
    }

    @Override
    public @NotNull Component getName() {
        return translatable("multibow.bowtype.admin_remove.name");
    }

    @Override
    public @NotNull List<Component> getLore() {
        return List.of(
                translatable("multibow.bowtype.admin_remove.lore.0", Style.style(NamedTextColor.DARK_PURPLE))
        );
    }

    @Override
    public boolean destroyOnHit() {
        return true;
    }

    @Override
    public void onHit(@NotNull ProjectileHitEvent event) {
        super.onHit(event);
        final var entity = event.getHitEntity();
        if(entity != null) entity.remove();
        final var block = event.getHitBlock();
        if(block != null) block.setType(Material.AIR);
    }
}
