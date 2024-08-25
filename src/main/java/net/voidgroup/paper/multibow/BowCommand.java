package net.voidgroup.paper.multibow;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.PropertyKey;

import java.util.Arrays;
import java.util.List;

import static net.voidgroup.paper.multibow.Util.translatable;

public class BowCommand implements TabCompleter, CommandExecutor {
    private final MultiBowPlugin plugin;
    private final Server server;
    private final BowTypeManager bowTypeManager;
    private final Permission otherPermission = new Permission("multibow.command.bow.other");
    public BowCommand(@NotNull final MultiBowPlugin plugin, @NotNull final Server server, @NotNull final BowTypeManager bowTypeManager) {
        this.plugin = plugin;
        this.server = server;
        this.bowTypeManager = bowTypeManager;
    }
    public void registerCommand(@NotNull final MultiBowPlugin plugin) {
        final var command = plugin.getCommand("bow");
        if(command == null) throw new NullPointerException("command is null");
        command.setTabCompleter(this);
        command.setExecutor(this);
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        var count = (int) Arrays.stream(args).filter(x -> !x.isEmpty()).count();
        if(args[args.length - 1].isEmpty()) count++;
        if(count == 1) return List.of("convert", "reset");
        return switch (args[0]) {
            case "convert" -> switch (count) {
                case 2 ->
                        bowTypeManager.getRegisteredBowTypes().entrySet()
                                .stream()
                                .filter(x -> sender.hasPermission(x.getValue().getPermission()))
                                .map(x -> x.getKey().asString()).toList();
                case 3 -> sender.hasPermission(otherPermission) ? null : List.of();
                default -> List.of();
            };
            case "reset" -> {
                if (count == 2 && sender.hasPermission(otherPermission)) yield null;
                yield List.of();
            }
            default -> List.of();
        };
    }

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if(args.length < 1) {
            sendError(sender, translatable("multibow.command.bow.unspecified_subcommand", Style.style(NamedTextColor.RED)));
            return false;
        }
        return switch (args[0]) {
            case "convert" -> onConvertCommand(sender, args);
            case "reset" -> onResetCommand(sender, args);
            default -> true;
        };
    }
    public boolean onResetCommand(@NotNull final CommandSender sender, @NotNull final String[] args) {
        final var player = getPlayer(sender, args, 1);
        if(player == null) return true;
        final var item = getBowItem(sender, player);
        if(item == null) return true;
        bowTypeManager.resetBow(item);
        sender.sendMessage(translatable("multibow.command.bow.reset_success").arguments(item.displayName()).hoverEvent(item));
        return true;
    }
    public boolean onConvertCommand(@NotNull final CommandSender sender, @NotNull final String[] args) {
        if(args.length < 2) {
            sendError(sender, translatable("multibow.command.bow.unspecified_type", Style.style(NamedTextColor.RED)));
            return false;
        }
        final var bowTypeKey = NamespacedKey.fromString(args[1], plugin);
        if(bowTypeKey == null) {
            sendError(sender, translatable("multibow.command.bow.invalid_type", Style.style(NamedTextColor.RED)));
            return true;
        }
        final var bowType = bowTypeManager.getRegisteredBowType(bowTypeKey);
        if(bowType == null) {
            sendError(sender, translatable("multibow.command.bow.unknown_type", Style.style(NamedTextColor.RED)));
            return true;
        }
        if(!sender.hasPermission(bowType.getPermission())) {
            sendError(sender, translatable("multibow.command.bow.no_permission", Style.style(NamedTextColor.RED)));
            return true;
        }
        final Player player = getPlayer(sender, args, 2);
        if(player == null) return true;
        final var item = getBowItem(sender, player);
        if(item == null) return true;
        bowTypeManager.convertBow(item, bowType);
        sender.sendMessage(translatable("multibow.command.bow.convert_success").arguments(item.displayName()).hoverEvent(item));
        return true;
    }
    private ItemStack getBowItem(@NotNull final CommandSender sender, @NotNull final Player player) {
        final var item = player.getInventory().getItemInMainHand();
        if(item.isEmpty()) {
            sendError(sender, translatable("multibow.command.bow.empty_hand", Style.style(NamedTextColor.RED)));
            return null;
        }
        if(item.getType() != Material.BOW) {
            sendError(sender, translatable("multibow.command.bow.not_bow", Style.style(NamedTextColor.RED)));
            return null;
        }
        return item;
    }
    private Player getPlayer(@NotNull final CommandSender sender, @NotNull final String[] args, final int index) {
        if(args.length < index + 1) {
            if(!(sender instanceof Player)) {
                sendError(sender, translatable("multibow.command.bow.unspecified_player", Style.style(NamedTextColor.RED)));
                return null;
            }
            return (Player) sender;
        }
        if(!sender.hasPermission(otherPermission)) {
            sendError(sender, translatable("multibow.command.bow.no_permission_other"));
            return null;
        }
        final var player = server.getPlayerExact(args[index]);
        if(player == null) sendError(sender, translatable("multibow.command.bow.unknown_player", Style.style(NamedTextColor.RED)));
        return player;
    }

    private static void sendError(@NotNull final CommandSender sender, @NotNull @PropertyKey(resourceBundle = "lang") final Component component) {
        sender.sendMessage(component.color(NamedTextColor.RED));
    }
}
