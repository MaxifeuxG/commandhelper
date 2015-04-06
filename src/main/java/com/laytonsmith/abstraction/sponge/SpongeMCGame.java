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
	private MCVersion ver;

	public SpongeMCGame() {
		game = CommandHelperSponge.self.myGame;
		server = new SpongeMCServer();
	}

	@Override
	public String getAPIVersion() {
		return game.getApiVersion();
	}

	@Override
	public String getImplementationVersion() {
		return game.getImplementationVersion();
	}

	@Override
	public MCVersion getMinecraftVersion() {
		if (ver == null) {
			ver = MCVersion.valueOf("MC" + game.getMinecraftVersion().getName().replace('.', '_'));
		}
		return ver;
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
	public MCServer getServer() {
		return server;
	}

	@Override
	public MCPlatform getPlatform() {
		return MCPlatform.valueOf(game.getPlatform().name());
	}

	@Override
	public Object getHandle() {
		return game;
	}

	public Game _Game() {
		return game;
	}
}
