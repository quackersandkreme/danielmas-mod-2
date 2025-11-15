package net.nightium.core;

import net.minecraft.entity.player.HungerManager;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;

public class PlayerInventoryData {
    private final ItemStack[] main;
    private final ItemStack[] armor;
    private final ItemStack offhand;
    private final int experienceLevel;
    private final float experienceProgress;
    private final int hunger;
    private final float saturation;

    public PlayerInventoryData(ServerPlayerEntity player) {
        this.main = copyMainInventory(player.getInventory().getMainStacks());
        this.armor = copyArmourInventory(player.getInventory().getStack(36), player.getInventory().getStack(37), player.getInventory().getStack(38), player.getInventory().getStack(39));
        this.offhand = copyOffhandInventory(player.getInventory().getStack(40));
        this.experienceLevel = player.experienceLevel;
        this.experienceProgress = player.experienceProgress;

        HungerManager hungerManager = player.getHungerManager();
        this.hunger = hungerManager.getFoodLevel();
        this.saturation = hungerManager.getSaturationLevel();
    }

    private ItemStack[] copyMainInventory(DefaultedList<ItemStack> inventory) {
        ItemStack[] copy = new ItemStack[inventory.size()];
        for (int i = 0; i < copy.length; i++) {
            copy[i] = inventory.get(i).copy();
        }
        return copy;
    }

    private ItemStack[] copyArmourInventory(ItemStack feet, ItemStack legs, ItemStack chest, ItemStack head) {
        ItemStack[] copy = new ItemStack[4];

        copy[0] = feet.copy();
        copy[1] = legs.copy();
        copy[2] = chest.copy();
        copy[3] = head.copy();

        return copy;
    }

    private ItemStack copyOffhandInventory(ItemStack inventory) {
        ItemStack copy;

        copy = inventory.copy();

        return copy;
    }

    public void applyTo(ServerPlayerEntity player) {
        applyInventory(player.getInventory().getMainStacks(), main);


        for (int i = 0; i < armor.length; i++) {
            player.getInventory().setStack(36+i, armor[i]);
        }

        player.getInventory().setStack(40, offhand);

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