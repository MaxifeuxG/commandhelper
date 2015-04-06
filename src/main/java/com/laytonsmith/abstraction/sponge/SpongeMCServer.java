package com.laytonsmith.abstraction.sponge;

import com.laytonsmith.abstraction.MCServer;
import org.spongepowered.api.Server;

/**
 * SpongeMCServer, 4/8/2015 4:45 PM
 *
 * @author jb_aero
 */
public class SpongeMCServer extends SpongeMCGame implements MCServer {

	@Override
	public Object getHandle() {
		return game.getServer();
	}

	public Server _Server() {
		return game.getServer();
	}
}
