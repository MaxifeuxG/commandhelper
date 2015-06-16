package com.laytonsmith.commandhelper;

import com.google.inject.Inject;
import com.laytonsmith.abstraction.Implementation;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.abstraction.StaticLayer;
import com.laytonsmith.abstraction.enums.MCChatColor;
import com.laytonsmith.abstraction.enums.sponge.SpongeMCEntityType;
import com.laytonsmith.abstraction.sponge.SpongeMCGame;
import com.laytonsmith.abstraction.sponge.entities.SpongeMCPlayer;
import com.laytonsmith.core.PomData;
import com.laytonsmith.core.Prefs;
import com.laytonsmith.core.SLLogger;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.UserManager;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.state.InitializationEvent;
import org.spongepowered.api.event.state.PostInitializationEvent;
import org.spongepowered.api.event.state.PreInitializationEvent;
import org.spongepowered.api.event.state.ServerStoppedEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.config.ConfigDir;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.source.ConsoleSource;
import org.spongepowered.api.util.command.spec.CommandExecutor;
import org.spongepowered.api.util.command.spec.CommandSpec;

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

		theGame.getCommandDispatcher().register(this, CommandSpec.builder()
				.description(Texts.of("Reload script files."))
				.permission(PomData.ARTIFACT_ID + ".reloadaliases")
				.arguments(GenericArguments.optional(GenericArguments.remainingJoinedStrings(Texts.of("settings"))))
				.executor(new CommandExecutor() {
					@Override
					public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
						MCPlayer player = null;
						if (src instanceof Player) {
							player = SpongeMCPlayer.Get((Player) src);
						}
						common.ac.reload(player, args.<String>getOne("settings").get().split(" "));
						return CommandResult.success();
					}
				}).build(), "reloadaliases", "reloadalias", "recompile");

		theGame.getCommandDispatcher().register(this, CommandSpec.builder()
				.description(Texts.of("Repeat the last command."))
				.permission(PomData.ARTIFACT_ID + ".repeat")
				.executor(new CommandExecutor() {
					@Override
					public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
						UserManager um = UserManager.GetUserManager(src.getName());
						if (um.getLastCommand() != null) {
							src.sendMessage(Texts.of(TextColors.GRAY + um.getLastCommand()));
							theGame.getCommandDispatcher().process(src, um.getLastCommand());
						} else {
							src.sendMessage(Texts.of(TextColors.RED + "No previous command."));
						}
						return CommandResult.success();
					}
				}).build(), "repeat", ".");

		theGame.getCommandDispatcher().register(this, CommandSpec.builder()
				.description(Texts.of("Unlocks interpreter mode for use ingame."))
				.executor(new CommandExecutor() {
					@Override
					public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
						if (src instanceof ConsoleSource) {
							int interpreterTimeout = Prefs.InterpreterTimeout();
							if (interpreterTimeout != 0) {
								common.interpreterUnlockedUntil = (interpreterTimeout * 60 * 1000) + System.currentTimeMillis();
								src.sendMessage(Texts.of("Inpterpreter mode unlocked for " + interpreterTimeout + " minute" + (
										interpreterTimeout == 1 ? "" : "s")));
							}
						} else {
							src.sendMessage(Texts.of(TextColors.RED, "This command can only be run from console."));
						}
						return CommandResult.success();
					}
				}).build(), "interpreter-on");

		theGame.getCommandDispatcher().register(this, CommandSpec.builder()
				.description(Texts.of())
				.permission(PomData.ARTIFACT_ID + ".interpreter")
				.executor(new CommandExecutor() {
					@Override
					public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
						if (src instanceof Player) {
							SpongeMCPlayer player = SpongeMCPlayer.Get((Player) src);
							if (Prefs.EnableInterpreter()) {
								if (Prefs.InterpreterTimeout() != 0) {
									if (common.interpreterUnlockedUntil < System.currentTimeMillis()) {
										player.sendMessage(MCChatColor.RED + "Interpreter mode is currently locked. Run \"interpreter-on\" from console to unlock it."
												+ " If you want to turn this off entirely, set the interpreter-timeout option to 0 in "
												+ CommandHelperFileLocations.getDefault().getPreferencesFile().getName());
										return CommandResult.success();
									}
								}
								common.startInterpret(player.getName());
								Static.SendMessage(player, MCChatColor.YELLOW + "You are now in interpreter mode. Type a dash (-) on a line by itself to exit, and >>> to enter"
										+ " multiline mode.");
							} else {
								Static.SendMessage(player, MCChatColor.RED + "The interpreter is currently disabled. Check your preferences file.");
							}
						} else {
							src.sendMessage(Texts.of("Interpreter mode can only be used as a player at this time."));
						}
						return CommandResult.success();
					}
				}).build(), "interpreter");

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
