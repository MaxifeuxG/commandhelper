package com.laytonsmith.abstraction;

import java.util.List;

public interface MCCommand extends AbstractionObject {

	List<String> getAliases();

	String getDescription();

	String getLabel();

	String getName();

	String getPermission();

	String getPermissionMessage();

	String getUsage();

	MCCommand setAliases(List<String> aliases);

	MCCommand setDescription(String desc);

	MCCommand setLabel(String name);

	MCCommand setPermission(String perm);

	MCCommand setPermissionMessage(String permmsg);

	MCCommand setUsage(String example);

	boolean testPermission(MCCommandSender target);

	boolean testPermissionSilent(MCCommandSender target);

	boolean register(MCCommandManager map);

	boolean isRegistered();

	boolean unregister(MCCommandManager map);

	MCPlugin getPlugin();

	Object getExecutor();

	MCPlugin getExecutingPlugin();

	Object getTabCompleter();

	MCPlugin getTabCompletingPlugin();

	void setExecutor(Object plugin);

	void setExecutingPlugin(MCPlugin plugin);

	void setTabCompleter(MCPlugin plugin);

	List<String> handleTabComplete(MCCommandSender sender, String alias, String[] args);

	boolean handleCustomCommand(MCCommandSender sender, String label, String[] args);
}
