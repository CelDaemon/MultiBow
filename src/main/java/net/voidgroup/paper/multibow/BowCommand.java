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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.PropertyKey;

import java.util.Arrays;
import java.util.List;

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
        if(count == 1) return List.of("convert", "reset");
        return switch (args[0]) {
            case "convert" -> switch (count) {
                case 2 ->
                        bowTypeManager.getRegisteredBowTypes().keySet().stream().map(NamespacedKey::asString).toList();
                case 3 -> null;
                default -> List.of();
            };
            case "reset" -> {
                if (count == 2) yield null;
                yield List.of();
            }
            default -> List.of();
        };
    }

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if(args.length < 1) {
            sendError(sender, "multibow.command.bow.unspecified_subcommand");
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
        sender.sendMessage(Component.translatable(translationKey("multibow.command.bow.reset_success"), item.displayName()).hoverEvent(item));
        return true;
    }
    public boolean onConvertCommand(@NotNull final CommandSender sender, @NotNull final String[] args) {
        if(args.length < 2) {
            sendError(sender, "multibow.command.bow.unspecified_type");
            return false;
        }
        final var bowTypeKey = NamespacedKey.fromString(args[1], plugin);
        if(bowTypeKey == null) {
            sendError(sender, "multibow.command.bow.invalid_type");
            return true;
        }
        final var bowType = bowTypeManager.getBowType(bowTypeKey);
        if(bowType == null) {
            sendError(sender, "multibow.command.bow.unknown_type");
            return true;
        }
        final Player player = getPlayer(sender, args, 2);
        if(player == null) return true;
        final var item = getBowItem(sender, player);
        if(item == null) return true;
        bowTypeManager.convertBow(item, bowType);
        sender.sendMessage(Component.translatable(translationKey("multibow.command.bow.convert_success"), item.displayName()).hoverEvent(item));
        return true;
    }
    private ItemStack getBowItem(final CommandSender sender, final Player player) {
        final var item = player.getInventory().getItemInMainHand();
        if(item.isEmpty()) {
            sendError(sender, "multibow.command.bow.empty_hand");
            return null;
        }
        if(item.getType() != Material.BOW) {
            sendError(sender, "multibow.command.bow.not_bow");
            return null;
        }
        return item;
    }
    private Player getPlayer(final CommandSender sender, final String[] args, final int index) {
        if(args.length < index + 1) {
            if(!(sender instanceof Player)) {
                sendError(sender, "multibow.command.bow.unspecified_player");
                return null;
            }
            return (Player) sender;
        }
        final var player = server.getPlayerExact(args[index]);
        if(player == null) sendError(sender, "multibow.command.bow.unknown_player");
        return player;
    }

    private static void sendError(final CommandSender sender, @PropertyKey(resourceBundle = "lang") final String translationKey) {
        sender.sendMessage(Component.translatable(translationKey, Style.style(NamedTextColor.RED)));
    }
}
