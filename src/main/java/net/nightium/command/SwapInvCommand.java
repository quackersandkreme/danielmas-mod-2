package net.nightium.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;

public class SwapInvCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        SwapInvCommands.register(dispatcher);
    }
}