package dev.john220099.GambleMod.module.impl;

import dev.john220099.GambleMod.module.Module;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import java.util.Random;

import static dev.john220099.GambleMod.utils.Maths.randomFloat;

public class ChestStealer extends Module {

    public ChestStealer() {
        super("ChestStealer", "Steal boxes.");
    }

    public boolean autoClose = true;
    private final Random random = new Random();
    private long nextClickTime;
    private int totalItems = 0;
    int remainingItems = 0;
    private boolean tracking = false;

    @Override
    public void onTick() {
        if (mc.player == null || mc.gameMode == null) {
            resetStealer();
            return;
        }

        Screen currentScreen = mc.gui.screen();

        if (!(currentScreen instanceof ContainerScreen containerScreen)) {
            resetStealer();
            return;
        }

        AbstractContainerMenu menu = containerScreen.getMenu();
        int containerSlotsSize = menu.slots.size() - 36;

        if (!tracking) {
            totalItems = 0;
            for (int i = 0; i < containerSlotsSize; i++) {
                if (menu.slots.get(i).hasItem()) {
                    totalItems++;
                }
            }
            remainingItems = totalItems;
            tracking = true;
        }

        remainingItems = 0;
        for (int i = 0; i < containerSlotsSize; i++) {
            if (menu.slots.get(i).hasItem()) {
                remainingItems++;
            }
        }

        long now = System.currentTimeMillis();
        if (now < nextClickTime) {
            return;
        }

        for (int i = 0; i < containerSlotsSize; i++) {
            Slot slot = menu.slots.get(i);
            if (!slot.hasItem()) {
                continue;
            }

            mc.gameMode.handleContainerInput(
                    menu.containerId,
                    slot.index,
                    0,
                    ContainerInput.QUICK_MOVE,
                    mc.player
            );

            nextClickTime = now + getDelay();
            return;
        }

        if (autoClose) {
            mc.player.closeContainer();
            mc.setScreenAndShow(null);
            resetStealer();
        }
    }

    private void resetStealer() {
        tracking = false;
        totalItems = 0;
        remainingItems = 0;
    }

    private long getDelay() {
        float min = randomFloat(0f, 250f);
        float max = randomFloat(100f, 300f);

        if (min > max) {
            float tmp = min;
            min = max;
            max = tmp;
        }

        return (long) (min + random.nextFloat() * (max - min));
    }
}
