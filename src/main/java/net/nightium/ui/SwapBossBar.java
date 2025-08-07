package net.nightium.ui;

import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class SwapBossBar {
    private final ServerBossBar bossBar;

    public SwapBossBar() {
        this.bossBar = new ServerBossBar(
                Text.literal("Inventory Swap Timer").formatted(Formatting.GOLD),
                BossBar.Color.BLUE,
                BossBar.Style.PROGRESS
        );
        this.bossBar.setPercent(1.0F);
    }

    public void addAllPlayers(MinecraftServer server) {
        server.getPlayerManager().getPlayerList().forEach(this.bossBar::addPlayer);
    }

    public void removeAllPlayers() {
        this.bossBar.clearPlayers();
    }

    public void update(int timeLeft, int totalTime) {
        float progress = (float) timeLeft / totalTime;
        this.bossBar.setPercent(progress);
        updateColor(progress);
        this.bossBar.setName(Text.literal("Next swap in: " + timeLeft + "s"));
    }

    private void updateColor(float progress) {
        BossBar.Color color = progress < 0.25f ? BossBar.Color.RED :
                progress < 0.5f ? BossBar.Color.YELLOW :
                        BossBar.Color.BLUE;
        this.bossBar.setColor(color);
    }
}