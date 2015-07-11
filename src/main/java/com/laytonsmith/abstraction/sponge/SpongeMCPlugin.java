package com.laytonsmith.abstraction.sponge;

import com.laytonsmith.abstraction.MCPlugin;
import org.spongepowered.api.plugin.PluginContainer;

/**
 * SpongeMCPlugin, 7/11/2015 12:22 AM
 *
 * @author jb_aero
 */
public class SpongeMCPlugin implements MCPlugin {

	final PluginContainer pluginContainer;
	private boolean enabled;

	public SpongeMCPlugin(PluginContainer pc) {
		this(pc, true);
	}

	public SpongeMCPlugin(PluginContainer pc, boolean isEnabled) {
		pluginContainer = pc;
		enabled = isEnabled;
	}

	@Override
	public PluginContainer getHandle() {
		return pluginContainer;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public boolean isInstanceOf(Class c) {
		return c.isInstance(getHandle().getInstance());
	}

	@Override
	public String getName() {
		return getHandle().getName();
	}

	@Override
	public String getId() {
		return getHandle().getId();
	}

	@Override
	public String getVersion() {
		return getHandle().getVersion();
	}
}
