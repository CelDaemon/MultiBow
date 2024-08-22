package net.voidgroup.paper.multibow;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.voidgroup.paper.multibow.bows.BowType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.PropertyKey;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static net.voidgroup.paper.multibow.Util.translationKey;

public class BowCommand implements TabCompleter, CommandExecutor {
    public BowCommand(final MultiBowPlugin plugin, final Server server, final BowTypeManager bowTypeManager) {
        this.plugin = plugin;
        this.server = server;
        this.bowTypeManager = bowTypeManager;
    }
    public void registerCommand(final MultiBowPlugin plugin) {
        final var command = plugin.getCommand("bow");
        if(command == null) throw new NullPointerException("command is null");
        command.setTabCompleter(this);
        command.setExecutor(this);
    }
    private final MultiBowPlugin plugin;
    private final Server server;
    private final BowTypeManager bowTypeManager;
    @Override
    public @Nullable List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        var count = (int) Arrays.stream(args).filter(x -> !x.isEmpty()).count();
        if(args[args.length - 1].isEmpty()) count++;
        final var bowKeys = bowTypeManager.getRegisteredBowTypes().keySet().stream().map(NamespacedKey::asString).collect(Collectors.toList());
        bowKeys.add("reset");
        return switch (count) {
            case 1 -> bowKeys;
            case 2 -> null;
            default -> List.of();
        };
    }

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if(args.length < 1) return sendError(sender, "multibow.command.bow.unspecified_type", true);
        final @Nullable BowType bowType;
        if(!args[0].equals("reset")) {
            final var bowKey = NamespacedKey.fromString(args[0], plugin);
            if(bowKey == null) return sendError(sender, "multibow.command.bow.invalid_type");
            bowType = bowTypeManager.getBowType(bowKey);
            if(bowType == null) return sendError(sender, "multibow.command.bow.unknown_type");
        } else bowType = null;
        final Player player;
        if(args.length < 2) {
            if(!(sender instanceof Player)) return sendError(sender, "multibow.command.bow.unspecified_player", true);
            player = (Player) sender;
        } else {
            player = server.getPlayerExact(args[1]);
            if(player == null) return sendError(sender, "multibow.command.bow.unknown_player");
        }
        final var item = player.getInventory().getItemInMainHand();
        if(item.isEmpty()) return sendError(sender, "multibow.command.bow.empty_hand");
        if(item.getType() != Material.BOW) return sendError(sender, "multibow.command.bow.not_bow");
        if(bowType != null) bowTypeManager.convertBow(item, bowType);
        else bowTypeManager.resetBow(item);
        sender.sendMessage(Component.translatable(translationKey("multibow.command.bow.success"), item.displayName()).hoverEvent(item));
        return true;
    }
    private static boolean sendError(final CommandSender sender, @PropertyKey(resourceBundle = "lang") final String translationKey) {
        return sendError(sender, translationKey, false);
    }
    private static boolean sendError(final CommandSender sender, @PropertyKey(resourceBundle = "lang") final String translationKey, final boolean printUsage) {
        sender.sendMessage(Component.translatable(translationKey, Style.style(NamedTextColor.RED)));
        return !printUsage;
    }
}
