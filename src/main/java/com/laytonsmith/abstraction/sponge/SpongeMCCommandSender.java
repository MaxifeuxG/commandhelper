package com.laytonsmith.abstraction.sponge;

import com.laytonsmith.abstraction.MCCommandSender;
import com.laytonsmith.abstraction.MCServer;
import com.laytonsmith.core.Static;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.util.command.CommandSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SpongeMCCommandSender, 6/8/2015 1:45 AM
 *
 * @author jb_aero
 */
public class SpongeMCCommandSender implements MCCommandSender {

	final CommandSource source;

	public SpongeMCCommandSender(CommandSource source) {
		this.source = source;
	}

	@Override
	public void sendMessage(String string) {
		getHandle().sendMessage((new TextBuilder.Literal(string)).build());
	}

	@Override
	public MCServer getServer() {
		return Static.getServer();
	}

	@Override
	public String getName() {
		return getHandle().getName();
	}

	@Override
	public boolean isOp() {
		return false;
	}

	@Override
	public boolean hasPermission(String perm) {
		return getHandle().hasPermission(perm);
	}

	@Override
	public boolean isPermissionSet(String perm) {
		return getHandle().getPermissionValue(getHandle().getActiveContexts(), perm) != Tristate.UNDEFINED;
	}

	@Override
	public List<String> getGroups() {
		ArrayList<String> groups = new ArrayList<>();
		for (Map.Entry<String, Boolean> e : getHandle().getSubjectData()
				.getPermissions(getHandle().getActiveContexts()).entrySet()) {
			if (e.getKey().startsWith(Static.groupPrefix) && e.getValue()) {
				groups.add(e.getKey().substring(Static.groupPrefix.length(), e.getKey().length()));
			}
		}
		return groups;
	}

	@Override
	public boolean inGroup(String groupName) {
		return hasPermission(Static.groupPrefix + "." + groupName);
	}

	@Override
	public CommandSource getHandle() {
		return source;
	}
}
