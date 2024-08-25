package net.voidgroup.paper.multibow.bows;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.voidgroup.paper.multibow.Util.translatable;
public class AdminRemoveBowType extends BowType {

    private final Permission immunityPermission = new Permission("multibow.bowtype.admin_remove.immune");
    public AdminRemoveBowType(@NotNull final PluginManager pluginManager) {
        super(pluginManager, new NamespacedKey("multibow", "admin_remove"), new Permission("multibow.bowtype.admin_remove"));
        pluginManager.addPermission(immunityPermission);
    }

    @Override
    public @NotNull Component getName() {
        return translatable("multibow.bowtype.admin_remove.name");
    }

    @Override
    public @NotNull List<Component> getLore() {
        return List.of(
                translatable("multibow.bowtype.admin_remove.lore.0", Style.style(NamedTextColor.DARK_PURPLE))
        );
    }
    @Override
    public @NotNull ItemRarity getRarity() {
        return ItemRarity.EPIC;
    }

    @Override
    public boolean destroyOnHit() {
        return true;
    }

    @Override
    public void onHit(@NotNull ProjectileHitEvent event) {
        super.onHit(event);
        final var entity = event.getHitEntity();
        if(entity != null) {
            if(entity instanceof final Player player) removePlayer(player);
            else entity.remove();
        }
    }

    public void removePlayer(Player player) {
        if(player.hasPermission(immunityPermission)) return;
        player.kick();
    }
}
