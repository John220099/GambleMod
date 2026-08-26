package dev.john220099.GambleMod.module;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class Module {
    private final String name;
    private final String description;
    private boolean enabled;
    protected final Minecraft mc = Minecraft.getInstance();
    
    public Module(String name, String description) {
        this.name = name;
        this.description = description;
        this.enabled = false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            if (enabled) {
                onEnable();
            } else {
                onDisable();
            }
        }
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    public void onRender2D(GuiGraphicsExtractor graphics, float partialTicks) {}

    protected void onEnable() {}

    protected void onDisable() {}

    public void onTick() {}
}