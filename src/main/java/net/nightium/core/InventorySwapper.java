package net.nightium.core;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventorySwapper {
    public static void swapInventories(MinecraftServer server) {
        List<ServerPlayerEntity> players = new ArrayList<>(server.getPlayerManager().getPlayerList());
        if (players.size() < 2) return;

        Collections.shuffle(players);
        List<PlayerInventoryData> inventories = saveInventories(players);
        applyInventories(players, inventories);
        notifyPlayers(players);
    }

    private static List<PlayerInventoryData> saveInventories(List<ServerPlayerEntity> players) {
        List<PlayerInventoryData> inventories = new ArrayList<>();
        for (ServerPlayerEntity player : players) {
            inventories.add(new PlayerInventoryData(player));
        }
        return inventories;
    }

    private static void applyInventories(List<ServerPlayerEntity> players, List<PlayerInventoryData> inventories) {
        for (int i = 0; i < players.size(); i++) {
            int nextIndex = (i + 1) % players.size();
            inventories.get(nextIndex).applyTo(players.get(i));
        }
    }

    private static void notifyPlayers(List<ServerPlayerEntity> players) {
        Text message = Text.literal("Your inventory has been swapped!").formatted(Formatting.YELLOW);
        for (ServerPlayerEntity player : players) {
            player.sendMessage(message, false);
        }
    }
}