package dev.john220099.GambleMod.gui;

import dev.john220099.GambleMod.module.Module;
import dev.john220099.GambleMod.module.ModuleManager;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GambleModScreen extends Screen {
    private final Screen parent;
    private String searchText = "";
    private final List<Button> moduleButtons = new ArrayList<>();
    private int scrollOffset = 0;

    public GambleModScreen(Screen parent) {
        super(Component.literal("GambleMod"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        EditBox searchBox = new EditBox(
                this.minecraft.font,
                this.width / 2 - 100, 30, 200, 20,
                Component.literal("Search modules...")
        );
        searchBox.setResponder(text -> {
            this.searchText = text.toLowerCase();
            this.scrollOffset = 0;
            refreshModules();
        });
        this.addRenderableWidget(searchBox);

        refreshModules();
    }

    private void refreshModules() {
        for (Button button : moduleButtons) {
            this.removeWidget(button);
        }
        moduleButtons.clear();

        List<Module> modules = ModuleManager.INSTANCE.getModules().stream()
                .filter(module -> searchText.isEmpty() ||
                        module.getName().toLowerCase().contains(searchText))
                .sorted(Comparator.comparing(Module::getName))
                .toList();

        int buttonWidth = 200;
        int buttonHeight = 20;
        int spacing = 25;
        int startY = 60;
        int maxButtons = (this.height - startY - 40) / spacing;

        int maxScroll = Math.max(0, modules.size() - maxButtons);
        scrollOffset = Math.min(scrollOffset, maxScroll);

        int endIndex = Math.min(modules.size(), scrollOffset + maxButtons);

        for (int i = scrollOffset; i < endIndex; i++) {
            Module module = modules.get(i);
            int x = (this.width - buttonWidth) / 2;
            int y = startY + ((i - scrollOffset) * spacing);

            Button button = Button.builder(
                            Component.literal(module.getName() + ": " +
                                    (module.isEnabled() ? "§aON" : "§cOFF")),
                            btn -> {
                                module.toggle();
                                btn.setMessage(Component.literal(module.getName() + ": " +
                                        (module.isEnabled() ? "§aON" : "§cOFF")));
                            }
                    ).bounds(x, y, buttonWidth, buttonHeight)
                    .tooltip(Tooltip.create(Component.literal(module.getDescription())))
                    .build();

            this.addRenderableWidget(button);
            moduleButtons.add(button);
        }

        Button doneButton = Button.builder(
                        Component.translatable("gui.done"),
                        _ -> this.onClose()
                ).bounds((this.width - buttonWidth) / 2, this.height - 30, buttonWidth, buttonHeight)
                .build();

        this.addRenderableWidget(doneButton);
        moduleButtons.add(doneButton);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (verticalAmount != 0) {
            scrollOffset = Math.max(0, scrollOffset - (int)verticalAmount);
            refreshModules();
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreenAndShow(parent);
    }
}