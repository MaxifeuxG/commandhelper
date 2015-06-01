package com.laytonsmith.abstraction.bukkit;

import com.laytonsmith.abstraction.MCCommandManager;
import com.laytonsmith.abstraction.MCEventManager;
import com.laytonsmith.abstraction.MCGame;
import com.laytonsmith.abstraction.MCPluginManager;
import com.laytonsmith.abstraction.enums.MCVersion;
import org.bukkit.Server;

/**
 * Created by jb_aero on 4/8/2015.
 */
public abstract class BukkitMCGame implements MCGame {

	Server server;
	MCVersion version;

	// Game is a Sponge concept, but it helps tidy up the Bukkit server
	public BukkitMCGame(Server game) {
		this.server = game;
	}

	@Override
	public Server getHandle() {
		return (Server) server;
	}

	@Override
	public String getAPIVersion() {
		return server.getBukkitVersion();
	}

	@Override
	public String getImplementationVersion() {
		return server.getVersion();
	}

	@Override
	public MCVersion getMinecraftVersion() {
		if (version == null) {
			int temp = s.getBukkitVersion().indexOf('-');
			version = MCVersion.match(s.getBukkitVersion().substring(0, temp).split("."));
		}
		return version;
	}

	@Override
	public MCCommandManager getCommandManager() {
		return null;
	}

	@Override
	public MCEventManager getEventManager() {
		return null;
	}

	@Override
	public MCPluginManager getPluginManager() {
		return null;
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
