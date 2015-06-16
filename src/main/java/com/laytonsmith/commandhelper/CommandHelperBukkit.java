// $Id$
/*
 * CommandHelper
 * Copyright (C) 2010 sk89q <http://www.sk89q.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.laytonsmith.commandhelper;

import com.laytonsmith.PureUtilities.Common.StringUtils;
import com.laytonsmith.abstraction.Implementation;
import com.laytonsmith.abstraction.MCCommand;
import com.laytonsmith.abstraction.MCCommandSender;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.abstraction.StaticLayer;
import com.laytonsmith.abstraction.bukkit.BukkitConvertor;
import com.laytonsmith.abstraction.bukkit.BukkitMCBlockCommandSender;
import com.laytonsmith.abstraction.bukkit.BukkitMCCommand;
import com.laytonsmith.abstraction.bukkit.entities.BukkitMCPlayer;
import com.laytonsmith.abstraction.enums.MCChatColor;
import com.laytonsmith.abstraction.enums.bukkit.BukkitMCEntityType;
import com.laytonsmith.core.JavaLogger;
import com.laytonsmith.core.Prefs;
import com.laytonsmith.core.Script;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.UserManager;
import com.laytonsmith.core.exceptions.ConfigCompileException;
import com.laytonsmith.core.exceptions.ConfigCompileGroupException;
import com.laytonsmith.persistence.DataSourceException;
import com.laytonsmith.persistence.ReadOnlyException;
import org.bukkit.Server;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This was originally known as CommandHelperPlugin.java -jb_aero
 *
 * Entry point for the plugin.
 *
 * @author sk89q
 */
public class CommandHelperBukkit extends JavaPlugin {
	//Do not rename this field, it is changed reflectively in unit tests.

	public static CommandHelperBukkit self;
	public CommandHelperCommon common;

	public CommandHelperBukkit() {
		self = this;
		common = new CommandHelperCommon(new JavaLogger(this.getLogger()));
	}

	/**
	 * Listener for the plugin system.
	 */
	final CommandHelperListener playerListener =
			new CommandHelperListener(common);
	/**
	 * Interpreter listener
	 */
	public final CommandHelperInterpreterListener interpreterListener =
			new CommandHelperInterpreterListener(common);
	/**
	 * Server Command Listener, for console commands
	 */
	final CommandHelperServerListener serverListener =
			new CommandHelperServerListener();
	final Set<MCPlayer> commandRunning = new HashSet<MCPlayer>();

	@Override
	public void onLoad() {
		Implementation.setServerType(Implementation.Type.BUKKIT);
		common.firstSetup(Server.class);
		BukkitMCEntityType.build();
	}

	/**
	 * Called on plugin enable.
	 */
	@Override
	public void onEnable() {

		//Metrics
		try {
			Metrics m = new Metrics(this);
			Metrics.Graph playerGraph = m.createGraph("Player Count");
			playerGraph.addPlotter(new Metrics.Plotter("Player Count") {

				@Override
				public int getValue() {
					return Static.getServer().getOnlinePlayers().size();
				}
			});
			m.start();
		} catch (IOException e) {
			// Failed to submit the stats :-(
		}

		common.secondSetup(getDescription().getVersion());
		common.hostCacheRefresh();

		BukkitDirtyRegisteredListener.PlayDirty();
		registerEvents(playerListener);

		//interpreter events
		registerEvents(interpreterListener);
		registerEvents(serverListener);

		//Script events
		StaticLayer.Startup(common);

		this.getLogger().log(Level.INFO, "[CommandHelper] CommandHelper {0} enabled", CommandHelperCommon.version);
	}

	/**
	 * Disables the plugin.
	 */
	@Override
	public void onDisable() {
		common.disable();
	}

	/**
	 * Register all events in a Listener class.
	 *
	 * @param listener
	 */
	public void registerEvents(Listener listener) {
		getServer().getPluginManager().registerEvents(listener, this);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		MCCommandSender mcsender = BukkitConvertor.BukkitGetCorrectSender(sender);
		MCCommand cmd = new BukkitMCCommand(command);
		return cmd.handleTabComplete(mcsender, alias, args);
	}

	/**
	 * Called when a command registered by this plugin is received.
	 *
	 * @param sender
	 * @param cmd
	 * @param commandLabel
	 * @param args
	 * @return
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		String cmdName = cmd.getName().toLowerCase();
		if ((sender.isOp() || (sender instanceof Player && (sender.hasPermission("commandhelper.reloadaliases")
				|| sender.hasPermission("ch.reloadaliases"))))
				&& (cmdName.equals("reloadaliases") || cmdName.equals("reloadalias") || cmdName.equals("recompile"))) {
			MCPlayer player = null;
			if (sender instanceof Player) {
				player = new BukkitMCPlayer((Player) sender);
			}
			common.ac.reload(player, args);
//            if(ac.reload(player)){
//                if(sender instanceof Player){
//                    Static.SendMessage(player, MCChatColor.GOLD + "Command Helper scripts sucessfully recompiled.");
//                }
//                System.out.println(TermColors.YELLOW + "Command Helper scripts sucessfully recompiled." + TermColors.reset());
//            } else{
//                if(sender instanceof Player){
//                    Static.SendMessage(player, MCChatColor.RED + "An error occured when trying to compile the script. Check the console for more information.");
//                }
//                System.out.println(TermColors.RED + "An error occured when trying to compile the script. Check the console for more information." + TermColors.reset());
//            }
			return true;
		} else if (cmdName.equals("commandhelper") && args.length >= 1 && args[0].equalsIgnoreCase("null")) {
			return true;
		} else if (cmdName.equals("runalias")) {
			//Hardcoded alias rebroadcast
			if (sender instanceof Player) {
				PlayerCommandPreprocessEvent pcpe = new PlayerCommandPreprocessEvent((Player) sender, StringUtils.Join(args, " "));
				playerListener.onPlayerCommandPreprocess(pcpe);
			} else if (sender instanceof ConsoleCommandSender) {
				String cmd2 = Static.strJoin(args, " ");
				if (cmd2.startsWith("/")) {
					cmd2 = cmd2.substring(1);
				}
				ServerCommandEvent sce = new ServerCommandEvent((ConsoleCommandSender) sender, cmd2);
				serverListener.onServerCommand(sce);
			} else if (sender instanceof BlockCommandSender) {
				MCCommandSender s = new BukkitMCBlockCommandSender((BlockCommandSender) sender);
				String cmd2 = StringUtils.Join(args, " ");
				Static.getAliasCore().alias(cmd2, s, new ArrayList<Script>());
			}
			return true;
		} else if (cmdName.equalsIgnoreCase("interpreter-on")) {
			if (sender instanceof ConsoleCommandSender) {
				int interpreterTimeout = Prefs.InterpreterTimeout();
				if (interpreterTimeout != 0) {
					common.interpreterUnlockedUntil = (interpreterTimeout * 60 * 1000) + System.currentTimeMillis();
					sender.sendMessage("Inpterpreter mode unlocked for " + interpreterTimeout + " minute" + (
							interpreterTimeout == 1 ? "" : "s"));
				}
			} else {
				sender.sendMessage("This command can only be run from console.");
			}
			return true;
		} else if (sender instanceof Player && java.util.Arrays.asList(new String[]{"commandhelper", "repeat",
				"viewalias", "delalias", "interpreter", "alias"}).contains(cmdName)) {
			try {
				return runCommand(new BukkitMCPlayer((Player) sender), cmdName, args);
			} catch (DataSourceException ex) {
				Logger.getLogger(CommandHelperBukkit.class.getName()).log(Level.SEVERE, null, ex);
			} catch (ReadOnlyException ex) {
				Logger.getLogger(CommandHelperBukkit.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IOException ex) {
				Logger.getLogger(CommandHelperBukkit.class.getName()).log(Level.SEVERE, null, ex);
			}
			return true;
		} else {
			MCCommandSender mcsender = BukkitConvertor.BukkitGetCorrectSender(sender);
			MCCommand mccmd = new BukkitMCCommand(cmd);
			return mccmd.handleCustomCommand(mcsender, commandLabel, args);
		}
	}

	/**
	 * Runs commands.
	 *
	 * @param player
	 * @param cmd
	 * @param args
	 * @return
	 */
	private boolean runCommand(final MCPlayer player, String cmd, String[] args) throws DataSourceException, ReadOnlyException, IOException {
		if (commandRunning.contains(player)) {
			return true;
		}

		commandRunning.add(player);
		UserManager um = UserManager.GetUserManager(player.getName());
		// Repeat command
		if (cmd.equals("repeat")) {
			if (player.isOp() || player.hasPermission("commandhelper.repeat")
					|| player.hasPermission("ch.repeat")) {
				//Go ahead and remove them, so that they can repeat aliases. They can't get stuck in
				//an infinite loop though, because the preprocessor won't try to fire off a repeat command
				commandRunning.remove(player);
				if (um.getLastCommand() != null) {
					Static.SendMessage(player, MCChatColor.GRAY + um.getLastCommand());
					execCommand(player, um.getLastCommand());
				} else {
					Static.SendMessage(player, MCChatColor.RED + "No previous command.");
				}
				return true;
			} else {
				Static.SendMessage(player, MCChatColor.RED + "You do not have permission to access the repeat command");
				commandRunning.remove(player);
				return true;
			}

			// Save alias
		} else if (cmd.equalsIgnoreCase("alias")) {
			if (!player.hasPermission("commandhelper.useralias") && !player.hasPermission("ch.useralias")) {
				Static.SendMessage(player, MCChatColor.RED + "You do not have permission to access the alias command");
				commandRunning.remove(player);
				return true;
			}

			if (args.length > 0) {
				String alias = Static.joinString(args, " ");
				try {
					int id = um.addAlias(alias, common.persistenceNetwork);
					if (id > -1) {
						Static.SendMessage(player, MCChatColor.YELLOW + "Alias added with id '" + id + "'");
					}
				} catch (ConfigCompileException ex) {
					Static.SendMessage(player, "Your alias could not be added due to a compile error:\n" + MCChatColor.RED + ex.getMessage());
				} catch (ConfigCompileGroupException e) {
					StringBuilder b = new StringBuilder();
					b.append("Your alias could not be added due to compile errors:\n").append(MCChatColor.RED);
					for (ConfigCompileException ex : e.getList()) {
						b.append(ex.getMessage());
					}
					Static.SendMessage(player, b.toString());
				}
			} else {
				//Display a help message
				Static.SendMessage(player, MCChatColor.GREEN + "Command usage: \n"
						+ MCChatColor.GREEN + "/alias <alias> - adds an alias to your user defined list\n"
						+ MCChatColor.GREEN + "/delalias <id> - deletes alias with id <id> from your user defined list\n"
						+ MCChatColor.GREEN + "/viewalias - shows you all of your aliases");
			}

			commandRunning.remove(player);
			return true;
			//View all aliases for this user
		} else if (cmd.equalsIgnoreCase("viewalias")) {
			if (!player.hasPermission("commandhelper.useralias") && !player.hasPermission("ch.useralias")) {
				Static.SendMessage(player, MCChatColor.RED + "You do not have permission to access the viewalias command");
				commandRunning.remove(player);
				return true;
			}
			int page = 0;
			try {
				page = Integer.parseInt(args[0]);
			} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
				//Meh. Index out of bounds, or number format exception. Whatever, show page 1
			}
			Static.SendMessage(player, um.getAllAliases(page, common.persistenceNetwork));
			commandRunning.remove(player);
			return true;
			// Delete alias
		} else if (cmd.equalsIgnoreCase("delalias")) {
			if (!player.hasPermission("commandhelper.useralias") && !player.hasPermission("ch.useralias")) {
				Static.SendMessage(player, MCChatColor.RED + "You do not have permission to access the delalias command");
				commandRunning.remove(player);
				return true;
			}
			try {
				ArrayList<String> deleted = new ArrayList<String>();
				for (String arg : args) {
					um.delAlias(Integer.parseInt(arg), common.persistenceNetwork);
					deleted.add("#" + arg);
				}
				if (args.length > 1) {
					String s = MCChatColor.YELLOW + "Aliases " + deleted.toString() + " were deleted";
					Static.SendMessage(player, s);

				} else {
					Static.SendMessage(player, MCChatColor.YELLOW + "Alias #" + args[0] + " was deleted");
				}
			} catch (NumberFormatException e) {
				Static.SendMessage(player, MCChatColor.RED + "The id must be a number");
			} catch (ArrayIndexOutOfBoundsException e) {
				Static.SendMessage(player, MCChatColor.RED + "Usage: /delalias <id> <id> ...");
			}
			commandRunning.remove(player);
			return true;

		} else if (cmd.equalsIgnoreCase("interpreter")) {
			if (player.hasPermission("commandhelper.interpreter")) {
				if (Prefs.EnableInterpreter()) {
					if (Prefs.InterpreterTimeout() != 0) {
						if (common.interpreterUnlockedUntil < System.currentTimeMillis()) {
							player.sendMessage(MCChatColor.RED + "Interpreter mode is currently locked. Run \"interpreter-on\" from console to unlock it."
									+ " If you want to turn this off entirely, set the interpreter-timeout option to 0 in "
									+ CommandHelperFileLocations.getDefault().getPreferencesFile().getName());
							commandRunning.remove(player);
							return true;
						}
					}
					common.startInterpret(player.getName());
					Static.SendMessage(player, MCChatColor.YELLOW + "You are now in interpreter mode. Type a dash (-) on a line by itself to exit, and >>> to enter"
							+ " multiline mode.");
				} else {
					Static.SendMessage(player, MCChatColor.RED + "The interpreter is currently disabled. Check your preferences file.");
				}
			} else {
				Static.SendMessage(player, MCChatColor.RED + "You do not have permission to run that command");
			}
			commandRunning.remove(player);
			return true;
		}
		commandRunning.remove(player);
		return false;
	}

	/**
	 * Execute a command.
	 *
	 * @param player
	 * @param cmd
	 */
	public static void execCommand(MCPlayer player, String cmd) {
		player.chat(cmd);
	}
}
