package com.laytonsmith.abstraction.sponge;

import com.laytonsmith.abstraction.MCCommand;
import com.laytonsmith.abstraction.MCCommandManager;
import com.laytonsmith.abstraction.MCCommandSender;
import com.laytonsmith.abstraction.MCPlugin;
import com.laytonsmith.commandhelper.CommandHelperCommon;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandExecutor;
import org.spongepowered.api.util.command.spec.CommandSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * SpongeMCCommand, 6/22/2015 4:42 PM
 *
 * @author jb_aero
 */
public class SpongeMCCommand implements MCCommand {

	private String name;
	private List<String> aliases;
	private String permission;
	private String description;

	public SpongeMCCommand(String name) {
		this.name = name;
		this.aliases = new ArrayList<>();
	}

	public SpongeMCCommand(CommandSpec source) {

	}

	@Override
	public List<String> getAliases() {
		return aliases;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getLabel() {
		return getName();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getPermission() {
		return permission;
	}

	@Override
	public String getPermissionMessage() {
		return "You don't have permission to use /" + getName();
	}

	@Override
	public String getUsage() {
		return null;
	}

	@Override
	public MCCommand setAliases(List<String> aliases) {
		this.aliases = aliases;
		this.aliases.add(0, name);
		return this;
	}

	@Override
	public MCCommand setDescription(String desc) {
		this.description = desc;
		return this;
	}

	@Override
	public MCCommand setLabel(String name) {
		this.name = name;
		return this;
	}

	@Override
	public MCCommand setPermission(String perm) {
		this.permission = perm;
		return this;
	}

	@Override
	public MCCommand setPermissionMessage(String permmsg) {
		return this;
	}

	@Override
	public MCCommand setUsage(String example) {
		return this;
	}

	@Override
	public boolean testPermission(MCCommandSender target) {
		return getHandle().testPermission(((SpongeMCCommandSender) target).getHandle());
	}

	@Override
	public boolean testPermissionSilent(MCCommandSender target) {
		return testPermission(target);
	}

	@Override
	public boolean register(MCCommandManager map) {
		return false;
	}

	@Override
	public boolean isRegistered() {
		return false;
	}

	@Override
	public boolean unregister(MCCommandManager map) {
		return false;
	}

	@Override
	public MCPlugin getPlugin() {
		return null;
	}

	@Override
	public MCPlugin getExecutor() {
		return null;
	}

	@Override
	public MCPlugin getExecutingPlugin() {
		return null;
	}

	@Override
	public MCPlugin getTabCompleter() {
		return null;
	}

	@Override
	public MCPlugin getTabCompletingPlugin() {
		return null;
	}

	@Override
	public void setExecutor(Object plugin) {

	}

	@Override
	public void setExecutingPlugin(MCPlugin plugin) {

	}

	@Override
	public void setTabCompleter(MCPlugin plugin) {

	}

	@Override
	public List<String> handleTabComplete(MCCommandSender sender, String alias, String[] args) {
		return null;
	}

	@Override
	public boolean handleCustomCommand(MCCommandSender sender, String label, String[] args) {
		return false;
	}

	@Override
	public CommandSpec getHandle() {
		return CommandSpec.builder().arguments(GenericArguments.remainingJoinedStrings(Texts.of("args")))
				.executor(new CommandExecutor() {
					@Override
					public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
						return CommandHelperCommon.self.handleCustomCommand(getName(),
								SpongeConvertor.SpongeGetCorrectSender(src), "unknown",
								args.getOne("args").isPresent() ? args.getOne("args").get().toString().split(" ") : new String[0])
								? CommandResult.success()
								: CommandResult.empty();
					}
				})
				.permission(getPermission())
				.description(Texts.of(getDescription()))
				.build();
	}
}
