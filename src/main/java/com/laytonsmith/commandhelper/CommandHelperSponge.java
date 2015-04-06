package com.laytonsmith.commandhelper;

import com.google.inject.Inject;
import com.laytonsmith.abstraction.Implementation;
import com.laytonsmith.abstraction.StaticLayer;
import com.laytonsmith.core.Static;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.state.InitializationEvent;
import org.spongepowered.api.event.state.PreInitializationEvent;
import org.spongepowered.api.event.state.ServerStoppedEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.util.event.Subscribe;

import java.util.logging.Level;

/**
 * Created by jb_aero on 4/5/2015.
 */
@Plugin(id = "commandhelper", name = "CommandHelper", version = "3.4.0")
public class CommandHelperSponge {

	public static CommandHelperSponge self;
	public CommandHelperMainClass common;
	public final Logger myLogger;
	public final Game myGame;

	@Inject
	public CommandHelperSponge(Logger logger, Game game) {
		myLogger = logger;
		myGame = game;
		self = this;
		common = new CommandHelperMainClass();
	}

	@Subscribe
	public void onPreInit(PreInitializationEvent event) {

		Implementation.setServerType(Implementation.Type.SPONGE);

		common.firstSetup(Game.class);
	}

	@Subscribe
	public void onInit(InitializationEvent event) {

		//BukkitDirtyRegisteredListener.PlayDirty();
		//registerEvents(playerListener);

		//interpreter events
		//registerEvents(interpreterListener);
		//registerEvents(serverListener);

		//Script events
		StaticLayer.Startup(common);

		//playerListener.loadGlobalAliases();
		//interpreterListener.reload();

		Static.getLogger().log(Level.INFO, "[CommandHelper] CommandHelper {0} enabled", common.version);
	}

	@Subscribe
	public void onStop(ServerStoppedEvent event) {
		common.disable();
	}
}
