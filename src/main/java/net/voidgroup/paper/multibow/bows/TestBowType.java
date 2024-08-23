package net.voidgroup.paper.multibow.bows;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemRarity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.voidgroup.paper.multibow.Util.translatable;

public class TestBowType extends BowType {
    public TestBowType(@NotNull NamespacedKey key) {
        super(key);
    }

    @Override
    public @NotNull Component getName() {
        return translatable("multibow.bowtype.test.name");
    }

    @Override
    public @NotNull List<Component> getLore() {
        return List.of(
                translatable("multibow.bowtype.test.lore.1", Style.style(NamedTextColor.DARK_PURPLE))
        );
    }

    @Override
    public @NotNull ItemRarity getRarity() {
        return ItemRarity.EPIC;
    }
}
