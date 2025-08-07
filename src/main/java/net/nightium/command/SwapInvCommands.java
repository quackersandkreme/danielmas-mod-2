package net.nightium.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.nightium.core.SwapManager;

public class SwapInvCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("swapinv")
                        .then(CommandManager.literal("start").executes(ctx -> {
                            SwapManager.startSwapping(ctx.getSource());
                            return 1;
                        }))
                        .then(CommandManager.literal("stop").executes(ctx -> {
                            SwapManager.stopSwapping(ctx.getSource());
                            return 1;
                        }))
                        .then(CommandManager.literal("interval")
                                .then(CommandManager.argument("seconds", IntegerArgumentType.integer(5))
                                        .executes(ctx -> {
                                            int seconds = IntegerArgumentType.getInteger(ctx, "seconds");
                                            SwapManager.setInterval(ctx.getSource(), seconds);
                                            return 1;
                                        })))
                        .executes(ctx -> {
                            ctx.getSource().sendFeedback(() ->
                                    Text.literal("SwapInventory commands:").formatted(Formatting.GOLD), false);
                            ctx.getSource().sendFeedback(() ->
                                    Text.literal("/swapinv start - Start inventory swapping"), false);
                            ctx.getSource().sendFeedback(() ->
                                    Text.literal("/swapinv stop - Stop inventory swapping"), false);
                            ctx.getSource().sendFeedback(() ->
                                    Text.literal("/swapinv interval <seconds> - Set swap interval (min 5)"), false);
                            ctx.getSource().sendFeedback(() ->
                                    Text.literal("Current status: " +
                                                    (SwapManager.isActive() ? "Active" : "Inactive") +
                                                    ", Interval: " + SwapManager.getInterval() + "s")
                                            .formatted(Formatting.YELLOW), false);
                            return 1;
                        })
        );
    }
}