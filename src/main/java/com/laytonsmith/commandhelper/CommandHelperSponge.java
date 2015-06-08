package com.laytonsmith.commandhelper;

import com.google.inject.Inject;
import com.laytonsmith.abstraction.Implementation;
import com.laytonsmith.abstraction.StaticLayer;
import com.laytonsmith.abstraction.enums.sponge.SpongeMCEntityType;
import com.laytonsmith.abstraction.sponge.SpongeMCGame;
import com.laytonsmith.core.PomData;
import com.laytonsmith.core.SLLogger;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.state.InitializationEvent;
import org.spongepowered.api.event.state.PostInitializationEvent;
import org.spongepowered.api.event.state.PreInitializationEvent;
import org.spongepowered.api.event.state.ServerStoppedEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.config.ConfigDir;

import java.io.File;

/**
 * CommandHelperSponge, 4/5/2015
 *
 * @author jb_aero
 */
@Plugin(id = PomData.ARTIFACT_ID, name = PomData.NAME, version = PomData.VERSION)
public class CommandHelperSponge {

	public static CommandHelperSponge self;
	public final CommandHelperCommon common;
	public final Game theGame;
	public final SpongeMCGame myGame;
	public final File myFolder;

	@Inject
	public CommandHelperSponge(Logger logger, Game game, @ConfigDir(sharedRoot = false) File configDir) {
		self = this;
		theGame = game;
		myGame = new SpongeMCGame();
		myFolder = configDir;
		common = new CommandHelperCommon(new SLLogger(logger));
	}

	@Subscribe
	public void onPreInit(PreInitializationEvent event) {
		Implementation.setServerType(Implementation.Type.SPONGE);
		CommandHelperFileLocations.setDefault(new CHSpongeFileLocations(myFolder));
		common.firstSetup(Game.class);
	}

	@Subscribe
	public void onInit(InitializationEvent event) {

		common.secondSetup(this.getClass().getAnnotation(Plugin.class).version());

		theGame.getEventManager().register(this, new CHSInterpretationListener(common));

		//Script events
		StaticLayer.Startup(common);

		common.logger.info("{0} {1} enabled", PomData.NAME, CommandHelperCommon.version);
	}

	@Subscribe
	public void onPostInit(PostInitializationEvent event) {
		SpongeMCEntityType.build(theGame.getRegistry());
	}

	@Subscribe
	public void onStop(ServerStoppedEvent event) {
		common.disable();
	}
}
