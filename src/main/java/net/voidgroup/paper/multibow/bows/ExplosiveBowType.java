package net.voidgroup.paper.multibow.bows;

import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import static net.voidgroup.paper.multibow.Util.translationKey;
public class ExplosiveBowType extends BowType {
    public ExplosiveBowType(NamespacedKey key) {
        super(key);
    }

    @Override
    public @NotNull Component getName() {
        return Component.translatable(translationKey("multibow.bowtype.explosive.name"));
    }
}
