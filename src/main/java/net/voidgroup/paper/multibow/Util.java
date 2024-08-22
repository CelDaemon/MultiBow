package net.voidgroup.paper.multibow;

import org.jetbrains.annotations.PropertyKey;

public class Util {
    public static String translationKey(@PropertyKey(resourceBundle = "lang") final String translationKey) {
        return translationKey;
    }

}
