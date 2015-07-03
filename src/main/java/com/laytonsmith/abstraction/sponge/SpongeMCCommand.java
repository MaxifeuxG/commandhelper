package com.laytonsmith.abstraction.sponge;

import com.laytonsmith.abstraction.MCCommand;
import com.laytonsmith.abstraction.MCCommandManager;
import com.laytonsmith.abstraction.MCCommandSender;
import com.laytonsmith.abstraction.MCPlugin;
import org.spongepowered.api.util.command.spec.CommandSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SpongeMCCommand, 6/22/2015 4:42 PM
 *
 * @author jb_aero
 */
public class SpongeMCCommand implements MCCommand {

	private String name;
	private ArrayList<String> aliases;
	private CommandSpec spec;

	public SpongeMCCommand(String name, CommandSpec build) {
		this.name = name;
		this.aliases = (ArrayList<String>) Arrays.asList(name);
		this.spec = build;
	}

	@Override
	public List<String> getAliases() {
		return aliases;
	}

	@Override
	public String getDescription() {
		return spec.getShortDescription(null).isPresent() ? spec.getShortDescription(null).get().toString()
				: "";
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
		return null;
	}

	@Override
	public String getPermissionMessage() {
		return "You don't have permission to use /" + getName();
	}

	@Override
	public String getUsage() {
		return spec.getUsage(null).toString();
	}

	@Override
	public MCCommand setAliases(List<String> aliases) {
		return null;
	}

	@Override
	public MCCommand setDescription(String desc) {
		return null;
	}

	@Override
	public MCCommand setLabel(String name) {
		this.name = name;
		return this;
	}

	@Override
	public MCCommand setPermission(String perm) {
		// TODO the following will not work
		spec = spec.builder().permission(perm).build();
		return this;
	}

	@Override
	public MCCommand setPermissionMessage(String permmsg) {
		return null;
	}

	@Override
	public MCCommand setUsage(String example) {
		return null;
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
		return spec;
	}
}
