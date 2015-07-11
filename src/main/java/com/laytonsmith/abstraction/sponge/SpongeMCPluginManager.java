package com.laytonsmith.abstraction.sponge;

import com.laytonsmith.abstraction.MCPlugin;
import com.laytonsmith.abstraction.MCPluginManager;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.plugin.PluginManager;

import java.util.ArrayList;
import java.util.List;

/**
 * SpongeMCPluginManager, 7/11/2015 12:16 AM
 *
 * @author jb_aero
 */
public class SpongeMCPluginManager implements MCPluginManager {

	final PluginManager pluginManager;

	public SpongeMCPluginManager(PluginManager pluginManager) {
		this.pluginManager = pluginManager;
	}

	@Override
	public PluginManager getHandle() {
		return pluginManager;
	}

	@Override
	public MCPlugin getPlugin(String name) {
		return null;
	}

	@Override
	public List<MCPlugin> getPlugins() {
		List<MCPlugin> ret = new ArrayList<>();
		for (PluginContainer pc : getHandle().getPlugins()) {
			ret.add(new SpongeMCPlugin(pc));
		}
		return ret;
	}

	@Override
	public boolean isLoaded(String id) {
		return getHandle().isLoaded(id);
	}
}
