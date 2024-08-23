package net.voidgroup.paper.multibow.bows;

import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

import static net.voidgroup.paper.multibow.Util.translatable;
public class AdminBanBowType extends BowType {
    public AdminBanBowType(@NotNull NamespacedKey key) {
        super(key);
    }

    @Override
    public @NotNull Component getName() {
        return translatable("multibow.bowtype.admin_ban.name");
    }

    @Override
    public @NotNull List<Component> getLore() {
        return List.of(
                translatable("multibow.bowtyle.admin_ban.lore.0")
        );
    }

    @Override
    public boolean destroyOnHit() {
        return true;
    }

    @Override
    public void onHit(@NotNull ProjectileHitEvent event) {
        super.onHit(event);
        if(!(event.getHitEntity() instanceof final Player player)) return;
        player.ban("Hit with the ban hammer (bow)",  (Date) null, "BOW >:3", true);
    }
}
