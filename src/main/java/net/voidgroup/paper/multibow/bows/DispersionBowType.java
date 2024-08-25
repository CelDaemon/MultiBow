package net.voidgroup.paper.multibow.bows;

import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

import static net.voidgroup.paper.multibow.Util.translatable;

public class DispersionBowType extends BowType {
    private static final Random random = new Random();
    public DispersionBowType() {
        super(new NamespacedKey("multibow", "dispersion"), new Permission("multibow.bowtype.dispersion"));
    }

    @Override
    public @NotNull Component getName() {
        return translatable("multibow.bowtype.dispersion.name");
    }

    @Override
    public @NotNull List<Component> getLore() {
        return List.of(
                translatable("multibow.bowtype.dispersion.lore.0")
        );
    }

    @Override
    public void onShoot(@NotNull EntityShootBowEvent event) {
        final var projectile = event.getProjectile();
        final var velocity = projectile.getVelocity();
        velocity.rotateAroundX(random.nextDouble() - 0.5d);
        velocity.rotateAroundY(random.nextDouble() - 0.5d);
        projectile.setVelocity(velocity);
        final var location = projectile.getLocation().setDirection(velocity);
        projectile.setRotation(-location.getYaw(), -location.getPitch());
    }
}
