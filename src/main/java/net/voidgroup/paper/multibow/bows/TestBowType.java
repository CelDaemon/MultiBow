package net.voidgroup.paper.multibow.bows;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemRarity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.voidgroup.paper.multibow.Util.translationKey;

public class TestBowType extends BowType {
    public TestBowType(NamespacedKey key) {
        super(key);
    }

    @Override
    public @NotNull Component getName() {
        return Component.translatable(translationKey("multibow.bowtype.test.name"));
    }

    @Override
    public @NotNull List<Component> getLore() {
        return List.of(
                Component.text("A bow for trying out the custom bow system", NamedTextColor.DARK_PURPLE)
        );
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.EPIC;
    }
}
