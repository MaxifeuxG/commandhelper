package com.laytonsmith.commandhelper;

import static com.laytonsmith.PureUtilities.Common.StringUtils.format;

import com.laytonsmith.abstraction.MCCommandSender;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.abstraction.StaticLayer;
import com.laytonsmith.abstraction.enums.MCChatColor;
import com.laytonsmith.abstraction.sponge.SpongeMCCommandSender;
import com.laytonsmith.abstraction.sponge.entities.SpongeMCPlayer;
import com.laytonsmith.abstraction.sponge.events.SpongeMiscEvents;
import com.laytonsmith.core.InternalException;
import com.laytonsmith.core.Script;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.UserManager;
import com.laytonsmith.core.events.Driver;
import com.laytonsmith.core.events.EventUtils;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.persistence.DataSourceException;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.PlayerChatEvent;
import org.spongepowered.api.event.entity.player.PlayerJoinEvent;
import org.spongepowered.api.event.entity.player.PlayerQuitEvent;
import org.spongepowered.api.event.message.CommandEvent;
import org.spongepowered.api.util.command.CommandResult;

import java.util.ArrayList;
import java.util.List;

/**
 * CHSInterpretationListener, 6/7/2015 7:10 PM
 *
 * @author jb_aero
 */
public class CHSInterpretationListener {

	private final CommandHelperCommon common;

	public CHSInterpretationListener(CommandHelperCommon common) {
		this.common = common;
	}

	@Subscribe
	public void onCommand(CommandEvent event) {
		//Run this first, so external events can intercept it.
		SpongeMiscEvents.SpongeMCCommandEvent cce = new SpongeMiscEvents.SpongeMCCommandEvent(event);
		EventUtils.TriggerExternal(cce);
		List<Script> userScripts;
		UserManager userManager;
		MCCommandSender user;
		if (event.getSource() instanceof Player) {
			user = new SpongeMCPlayer((Player) event.getSource());
			// They are in interpreter mode, so we want it to handle this, not everything else.
			// This was the original spec behavior, but I don't see any reason for it - jb_aero
			if (common.isInInterpreterMode(event.getSource().getName())) {
				common.textLine((MCPlayer) user, format("/{0} {1}", event.getCommand(), event.getArguments()));
				event.setCancelled(true);
				return;
			}
			EventUtils.TriggerListener(Driver.PLAYER_COMMAND, "player_command", cce);
			if (cce.isCancelled()) {
				return;
			}
			String id = ((Player) event.getSource()).getUniqueId().toString().replace("-", "");
			userManager = UserManager.GetUserManager(id);
			// leave this player-only until it can be proven to work serverside
			if (event.getCommand().equals(".") || event.getCommand().equals("repeat")) {
				return;
			}
			userManager.setLastCommand(event.getCommand());
		} else {
			// commandblocks && remote will share with console
			userManager = UserManager.GetUserManager("CONSOLE");
			user = new SpongeMCCommandSender(event.getSource());
		}

		try {
			userScripts = userManager.getAllScripts(common.getPersistenceNetwork());
		} catch (DataSourceException e) {
			common.logger.error(e.getMessage());
			// fall back to global aliases
			userScripts = new ArrayList<>();
		}

		boolean match = false;
		try {
			match = Static.getAliasCore().alias(format("/{0} {1}", event.getCommand(), event.getArguments(),
					user, userScripts), user, userScripts);
		} catch (InternalException e) {
			Static.getLogger().error(e.getMessage());
		} catch (ConfigRuntimeException e) {
			Static.getLogger().warn(e.getMessage());
		} catch (Throwable e) {
			user.sendMessage(MCChatColor.RED + "Command failed with following reason: " + e.getMessage());
			//Obviously the command is registered, but it somehow failed. Cancel the event.
			e.printStackTrace();
			return;
		}
		if (match) {
			event.setCancelled(true);
			event.setResult(CommandResult.success());
		}
	}

	@Subscribe
	public void onPlayerChat(final PlayerChatEvent event) {
		if (common.isInInterpreterMode(event.getUser().getName())) {
			final MCPlayer p = new SpongeMCPlayer(event.getUser());
			event.setCancelled(true);
			StaticLayer.SetFutureRunnable(null, 0, new Runnable() {

				@Override
				public void run() {
					common.textLine(p, event.getMessage().toString());
				}
			});
		}
	}

	@Subscribe
	public void onPlayerQuit(PlayerQuitEvent event) {
		UserManager.ClearUser(event.getUser().getName());
		common.stopInterpret(event.getUser().getName());
		common.multilineMode.remove(event.getUser().getName());
		CommandHelperSponge.self.myGame.getAbstractServer().deCachePlayer(event.getUser());
	}

	@Subscribe
	public void onPlayerJoin(PlayerJoinEvent event) {
		SpongeMCPlayer player = SpongeMCPlayer.Get(event.getUser());
		Static.SetPlayerHost(player, player.getHost());
		Static.HostnameCache(player);
	}
}
