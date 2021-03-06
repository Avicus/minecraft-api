package net.avicus.minecraft.api.plugin;

import java.util.Collection;

public interface PluginFinder {

    Plugin getPlugin(String name);

    Collection<? extends Plugin> getAllPlugins();
}
