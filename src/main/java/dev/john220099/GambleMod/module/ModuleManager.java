package dev.john220099.GambleMod.module;

import dev.john220099.GambleMod.module.impl.*;
import dev.john220099.GambleMod.module.impl.Jesus;
import dev.john220099.GambleMod.module.impl.NoRender;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ModuleManager {
    public static final ModuleManager INSTANCE = new ModuleManager();
    private final List<Module> modules = new ArrayList<>();

    private ModuleManager() {
        register(new TriggerBot());
        register(new AutoSprint());
        register(new BridgeAssist());
        register(new FastUse());
        register(new Jesus());
        register(new CrystalClicker());
        register(new NoRender());
        register(new OpSec());
        register(new FullBright());
        register(new CameraFix());
        register(new ESP());

        modules.sort(Comparator.comparing(Module::getName));
    }

    public void register(Module module) {
        modules.add(module);
    }

    public List<Module> getModules() {
        return modules;
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(Class<T> moduleClass) {
        for (Module module : modules) {
            if (module.getClass().equals(moduleClass)) {
                return (T) module;
            }
        }
        return null;
    }

    public void onTick() {
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.onTick();
            }
        }
    }
}