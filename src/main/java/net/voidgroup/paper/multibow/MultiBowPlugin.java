package net.voidgroup.paper.multibow;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.util.UTF8ResourceBundleControl;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;
import java.util.ResourceBundle;

public class MultiBowPlugin extends JavaPlugin {
    private final TranslationRegistry translationRegistry = TranslationRegistry.create(Key.key("multibow", "translation"));
    private final ResourceBundle bundle = ResourceBundle.getBundle("lang", Locale.US, UTF8ResourceBundleControl.get());
    private final Server server = getServer();
    private final PluginManager pluginManager = server.getPluginManager();

    private final BowTypeManager bowTypeManager = new BowTypeManager(this, pluginManager);

    private final BowEventHandler bowEventHandler = new BowEventHandler(this, pluginManager, bowTypeManager);
    private final BowCommand bowCommand = new BowCommand(this, server, bowTypeManager);
    @Override
    public void onEnable() {
        translationRegistry.registerAll(Locale.US, bundle, true);
        GlobalTranslator.translator().addSource(translationRegistry);
        bowTypeManager.registerBowTypes();
        bowEventHandler.registerEvents();
        bowCommand.registerCommand(this);
    }
}
