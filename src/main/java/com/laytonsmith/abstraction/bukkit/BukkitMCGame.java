package com.laytonsmith.abstraction.bukkit;

import com.laytonsmith.PureUtilities.Common.ReflectionUtils;
import com.laytonsmith.abstraction.MCCommandManager;
import com.laytonsmith.abstraction.MCEventManager;
import com.laytonsmith.abstraction.MCGame;
import com.laytonsmith.abstraction.MCPluginManager;
import com.laytonsmith.abstraction.enums.MCVersion;
import org.bukkit.Server;
import org.bukkit.command.SimpleCommandMap;

/**
 * Created by jb_aero on 4/8/2015.
 */
public abstract class BukkitMCGame implements MCGame {

	final Server server;
	final MCVersion version;
	final BukkitMCPluginManager pluginManager;

	// Game is a Sponge concept, but it helps tidy up the Bukkit server
	public BukkitMCGame(Server game) {
		this.server = game;
		int temp = server.getBukkitVersion().indexOf('-');
		version = MCVersion.match(getHandle().getBukkitVersion().substring(0, temp).split("."));
		pluginManager = new BukkitMCPluginManager(server.getPluginManager());
	}

	@Override
	public Server getHandle() {
		return server;
	}

	@Override
	public String getAPIVersion() {
		return getHandle().getBukkitVersion();
	}

	@Override
	public String getImplementationVersion() {
		return getHandle().getVersion();
	}

	@Override
	public MCVersion getMinecraftVersion() {
		return version;
	}

	@Override
	public MCCommandManager getCommandManager() {
		return new BukkitMCCommandManager((SimpleCommandMap) ReflectionUtils.invokeMethod(server.getClass(), server, "getCommandMap"));
	}

	@Override
	public MCEventManager getEventManager() {
		return pluginManager;
	}

	@Override
	public MCPluginManager getPluginManager() {
		return pluginManager;
	}

	@Override
	public Object getServiceManger() {
		return null;
	}

	@Override
	public Object getGameRegistry() {
		return null;
	}

	@Override
	public Object getClient() {
		return null;
	}

	@Override
	public MCPlatform getPlatform() {
		return MCPlatform.SERVER;
	}
}
