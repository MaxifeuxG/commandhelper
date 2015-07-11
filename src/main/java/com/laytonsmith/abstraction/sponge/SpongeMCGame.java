package com.laytonsmith.abstraction.sponge;

import com.laytonsmith.abstraction.MCCommandManager;
import com.laytonsmith.abstraction.MCEventManager;
import com.laytonsmith.abstraction.MCGame;
import com.laytonsmith.abstraction.MCPluginManager;
import com.laytonsmith.abstraction.MCServer;
import com.laytonsmith.abstraction.enums.MCVersion;
import com.laytonsmith.commandhelper.CommandHelperSponge;
import org.spongepowered.api.Game;

/**
 * SpongeMCGame, 4/8/2015 4:29 PM
 *
 * @author jb_aero
 */
public class SpongeMCGame implements MCGame {

	protected final Game game;
	private final SpongeMCServer server;
	private SpongeMCCommandManager commandManager;
	private final SpongeMCPluginManager pluginManager;
	private final SpongeMCEventManager eventManager;
	private final MCVersion ver;

	public SpongeMCGame() {
		game = CommandHelperSponge.self.theGame;
		ver = MCVersion.match(_Game().getPlatform().getMinecraftVersion().getName().split("\\."));
		if (!(this instanceof MCServer)) {
			server = new SpongeMCServer();
		} else {
			server = null;
		}
		pluginManager = new SpongeMCPluginManager(_Game().getPluginManager());
		eventManager = new SpongeMCEventManager(_Game().getEventManager());
	}

	@Override
	public String getAPIVersion() {
		return _Game().getPlatform().getApiVersion();
	}

	@Override
	public String getImplementationVersion() {
		return _Game().getPlatform().getVersion();
	}

	@Override
	public MCVersion getMinecraftVersion() {
		return ver;
	}

	@Override
	public MCCommandManager getCommandManager() {
		if (commandManager == null) {
			commandManager = new SpongeMCCommandManager(_Game().getCommandDispatcher());
		}
		return commandManager;
	}

	@Override
	public MCEventManager getEventManager() {
		return eventManager;
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
	public MCServer getServer() {
		return server;
	}

	public SpongeMCServer getAbstractServer() {
		return server;
	}

	@Override
	public MCPlatform getPlatform() {
		return MCPlatform.valueOf(game.getPlatform().getType().name());
	}

	@Override
	public Object getHandle() {
		return game;
	}

	public Game _Game() {
		return game;
	}

	@Override
	public int hashCode() {
		return getHandle().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return getHandle().equals(obj);
	}

	@Override
	public String toString() {
		return getHandle().toString();
	}
}
