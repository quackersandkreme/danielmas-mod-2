package net.nightium.core;

import net.minecraft.entity.player.HungerManager;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;

public class PlayerInventoryData {
    private final ItemStack[] main;
    private final ItemStack[] armor;
    private final ItemStack[] offhand;
    private final int experienceLevel;
    private final float experienceProgress;
    private final int hunger;
    private final float saturation;

    public PlayerInventoryData(ServerPlayerEntity player) {
        this.main = copyInventory(player.getInventory().main);
        this.armor = copyInventory(player.getInventory().armor);
        this.offhand = copyInventory(player.getInventory().offHand);
        this.experienceLevel = player.experienceLevel;
        this.experienceProgress = player.experienceProgress;

        HungerManager hungerManager = player.getHungerManager();
        this.hunger = hungerManager.getFoodLevel();
        this.saturation = hungerManager.getSaturationLevel();
    }

    private ItemStack[] copyInventory(DefaultedList<ItemStack> inventory) {
        ItemStack[] copy = new ItemStack[inventory.size()];
        for (int i = 0; i < copy.length; i++) {
            copy[i] = inventory.get(i).copy();
        }
        return copy;
    }

    public void applyTo(ServerPlayerEntity player) {
        applyInventory(player.getInventory().main, main);
        applyInventory(player.getInventory().armor, armor);
        applyInventory(player.getInventory().offHand, offhand);

        player.experienceLevel = this.experienceLevel;
        player.experienceProgress = this.experienceProgress;

        HungerManager hungerManager = player.getHungerManager();
        hungerManager.setFoodLevel(this.hunger);
        hungerManager.setSaturationLevel(this.saturation);

        player.playerScreenHandler.sendContentUpdates();
    }

    private void applyInventory(DefaultedList<ItemStack> target, ItemStack[] source) {
        for (int i = 0; i < source.length && i < target.size(); i++) {
            target.set(i, source[i].copy());
        }
    }
}