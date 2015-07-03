package com.laytonsmith.abstraction.sponge;

import com.google.common.base.Optional;
import com.laytonsmith.abstraction.MCCommand;
import com.laytonsmith.abstraction.MCCommandManager;
import com.laytonsmith.commandhelper.CommandHelperCommon;
import com.laytonsmith.commandhelper.CommandHelperSponge;
import org.spongepowered.api.service.command.CommandService;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandMapping;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import java.util.List;

/**
 * SpongeMCCommandManager, 6/22/2015 2:55 PM
 *
 * @author jb_aero
 */
public class SpongeMCCommandManager implements MCCommandManager {

	private final CommandService field;

	public SpongeMCCommandManager(CommandService commandDispatcher) {
		this.field = commandDispatcher;
	}

	@Override
	public void clearCommands() {
		for (CommandMapping cm : getHandle().getCommands()) {
			getHandle().removeMapping(cm);
		}
	}

	@Override
	public boolean isCommand(String name) {
		return getHandle().get(name).isPresent();
	}

	@Override
	public MCCommand getCommand(String name) {
		return null;
	}

	@Override
	public List<MCCommand> getCommands() {
		return null;
	}

	@Override
	public boolean register(String fallback, MCCommand cmd) {
		Optional<? extends CommandMapping> registered = getHandle().register(CommandHelperSponge.self,
				(CommandCallable) cmd.getHandle(), (String[]) cmd.getAliases().toArray());
		if (registered.isPresent()) {
			((NamedExecutor) cmd.getExecutor()).setName(registered.get().getPrimaryAlias());
			return true;
		}
		return false;
	}

	@Override
	public boolean register(String label, String fallback, MCCommand cmd) {
		return register(fallback, cmd);
	}

	@Override
	public boolean unregister(MCCommand cmd) {
		Optional<? extends CommandMapping> found = getHandle().get(cmd.getName());
		return found.isPresent() && getHandle().removeMapping(found.get()).isPresent();
	}

	@Override
	public CommandService getHandle() {
		return field;
	}

	public class NamedExecutor implements CommandExecutor {

		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
			return CommandHelperCommon.self.handleCustomCommand(getName(),
					SpongeConvertor.SpongeGetCorrectSender(src), "unknown",
					args.getOne("args").isPresent() ? args.getOne("args").get().toString().split(" ") : new String[0])
					? CommandResult.success()
					: CommandResult.empty();
		}
	}
}
