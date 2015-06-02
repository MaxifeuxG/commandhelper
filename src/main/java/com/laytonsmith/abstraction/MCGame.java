package com.laytonsmith.abstraction;

import com.laytonsmith.abstraction.enums.MCVersion;

/**
 * Created by jb_aero on 4/6/2015.
 */
public interface MCGame extends AbstractionObject {

	String getAPIVersion();

	String getImplementationVersion();

	MCVersion getMinecraftVersion();

	MCCommandManager getCommandManager();

	MCEventManager getEventManager();

	MCPluginManager getPluginManager();

	Object getServiceManger();

	Object getGameRegistry();

	Object getClient();

	MCServer getServer();

	MCPlatform getPlatform();

	enum MCPlatform {
		CLIENT,
		SERVER,
		UNKNOWN
	}

}
