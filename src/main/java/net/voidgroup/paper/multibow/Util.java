package net.voidgroup.paper.multibow;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

public class Util {
    public static TranslatableComponent translatable(@NotNull @PropertyKey(resourceBundle = "lang") final String key) {
        return Component.translatable(key);
    }
    public static TranslatableComponent translatable(@NotNull @PropertyKey(resourceBundle = "lang") final String key, @NotNull Style style) {
        return Component.translatable(key, style);
    }

}
