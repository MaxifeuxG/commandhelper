package com.laytonsmith.abstraction.bukkit;

import com.laytonsmith.PureUtilities.Common.ReflectionUtils;
import com.laytonsmith.abstraction.MCCommand;
import com.laytonsmith.abstraction.MCCommandManager;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author jb_aero
 */
public class BukkitMCCommandManager implements MCCommandManager {

	SimpleCommandMap scm;

	public BukkitMCCommandManager(SimpleCommandMap invokeMethod) {
		scm = invokeMethod;
	}

	@Override
	public Object getHandle() {
		return scm;
	}

	@Override
	public void clearCommands() {
		scm.clearCommands();
	}
	
	@Override
	public boolean isCommand(String name) {
		return scm.getCommand(name) != null;
	}

	@Override
	public MCCommand getCommand(String name) {
		return scm.getCommand(name) == null ? null : new BukkitMCCommand(scm.getCommand(name));
	}

	@Override
	public List<MCCommand> getCommands() {
		List<MCCommand> cmds = new ArrayList<MCCommand>();
		for (Command c : scm.getCommands()) {
			cmds.add(new BukkitMCCommand(c));
		}
		return cmds;
	}

	@Override
	public boolean register(String fallback, MCCommand cmd) {
		return scm.register(fallback, ((BukkitMCCommand) cmd).cmd);
	}

	@Override
	public boolean register(String label, String fallback, MCCommand cmd) {
		return scm.register(label, fallback, ((BukkitMCCommand) cmd).cmd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean unregister(MCCommand cmd) {
		if (cmd.isRegistered()) {
			((Map<String,Command>) ReflectionUtils.get(scm.getClass(), scm, "knownCommands")).remove(cmd.getName());
			return cmd.unregister(this);
		} else {
			return false;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		return scm.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return scm.hashCode();
	}
	
	@Override
	public String toString() {
		return scm.toString();
	}
}
