package net.voidgroup.paper.multibow;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.util.UTF8ResourceBundleControl;
import net.voidgroup.paper.multibow.bows.ExplosiveBowType;
import net.voidgroup.paper.multibow.bows.TestBowType;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.intellij.lang.annotations.Subst;

import java.util.Locale;
import java.util.ResourceBundle;

public class MultiBowPlugin extends JavaPlugin {
    @Subst("multibow")
    private final String namespace = this.getName().toLowerCase();
    private final TranslationRegistry translationRegistry = TranslationRegistry.create(Key.key(namespace, "translation"));
    private final ResourceBundle bundle = ResourceBundle.getBundle("lang", Locale.US, UTF8ResourceBundleControl.get());
    private final Server server = getServer();
    private final PluginManager manager = server.getPluginManager();
    private final BowTypeManager bowTypeManager = new BowTypeManager(this);
    private final BowCommand bowCommand = new BowCommand(this, server, bowTypeManager);
    @Override
    public void onEnable() {
        translationRegistry.registerAll(Locale.US, bundle, true);
        GlobalTranslator.translator().addSource(translationRegistry);
        bowTypeManager.registerBowType(new TestBowType(new NamespacedKey(this, "test")));
        bowTypeManager.registerBowType(new ExplosiveBowType(new NamespacedKey(this, "explosive")));
        bowCommand.registerCommand(this);
    }
}
