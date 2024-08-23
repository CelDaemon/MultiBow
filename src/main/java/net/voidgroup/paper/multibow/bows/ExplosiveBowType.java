package net.voidgroup.paper.multibow.bows;

import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import static net.voidgroup.paper.multibow.Util.translatable;
public class ExplosiveBowType extends BowType {
    public ExplosiveBowType(NamespacedKey key) {
        super(key);
    }

    @Override
    public @NotNull Component getName() {
        return translatable("multibow.bowtype.explosive.name");
    }
}
