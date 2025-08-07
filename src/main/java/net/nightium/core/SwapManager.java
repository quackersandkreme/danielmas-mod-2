package net.nightium.core;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.nightium.ui.SwapBossBar;

public class SwapManager {
    private static boolean isActive = false;
    private static int interval = 60;
    private static int timer = 0;
    private static SwapBossBar bossBar;

    public static void onServerTick(MinecraftServer server) {
        if (!isActive || server.getTicks() % 20 != 0) return;

        timer++;
        updateBossBar();

        if (timer >= interval) {
            timer = 0;
            InventorySwapper.swapInventories(server);
        }
    }

    public static void startSwapping(ServerCommandSource source) {
        if (isActive) {
            source.sendError(Text.literal("Inventory swapping is already active!"));
            return;
        }

        isActive = true;
        timer = 0;
        bossBar = new SwapBossBar();
        bossBar.addAllPlayers(source.getServer());

        source.sendFeedback(() -> Text.literal("Started inventory swapping every " + interval + " seconds!")
                .formatted(Formatting.GREEN), true);
    }

    public static void stopSwapping(ServerCommandSource source) {
        if (!isActive) {
            source.sendError(Text.literal("Inventory swapping is not active!"));
            return;
        }

        isActive = false;
        if (bossBar != null) {
            bossBar.removeAllPlayers();
            bossBar = null;
        }

        source.sendFeedback(() -> Text.literal("Stopped inventory swapping!")
                .formatted(Formatting.RED), true);
    }

    public static void setInterval(ServerCommandSource source, int seconds) {
        interval = seconds;
        timer = 0;
        source.sendFeedback(() -> Text.literal("Set swap interval to " + seconds + " seconds!")
                .formatted(Formatting.GREEN), true);
    }

    private static void updateBossBar() {
        if (bossBar != null) {
            bossBar.update(interval - timer, interval);
        }
    }

    public static boolean isActive() {
        return isActive;
    }

    public static int getInterval() {
        return interval;
    }
}