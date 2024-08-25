package net.voidgroup.paper.multibow.bows;

import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

import static net.voidgroup.paper.multibow.Util.translatable;
public class AdminBanBowType extends BowType {
    private final Permission immunityPermission = new Permission("multibow.bowtype.admin_ban.immune");
    public AdminBanBowType(final PluginManager pluginManager) {
        super(pluginManager, new NamespacedKey("multibow", "admin_ban"), new Permission("multibow.bowtype.admin_ban"));
        pluginManager.addPermission(immunityPermission);
    }

    @Override
    public @NotNull Component getName() {
        return translatable("multibow.bowtype.admin_ban.name");
    }

    @Override
    public @NotNull List<Component> getLore() {
        return List.of(
                translatable("multibow.bowtype.admin_ban.lore.0")
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
        if(!(event.getHitEntity() instanceof final Player hitPlayer) || hitPlayer.hasPermission(immunityPermission)) return;
        hitPlayer.ban("Hit with the ban hammer (bow)",  (Date) null, "BOW >:3", true);
    }
}
