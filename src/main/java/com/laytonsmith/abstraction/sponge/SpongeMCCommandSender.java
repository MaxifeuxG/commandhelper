package com.laytonsmith.abstraction.sponge;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.laytonsmith.abstraction.MCCommandSender;
import com.laytonsmith.abstraction.MCServer;
import com.laytonsmith.commandhelper.CommandHelperSponge;
import com.laytonsmith.core.Static;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.util.command.CommandSource;

import java.util.List;

import javax.annotation.Nullable;

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
		final SubjectCollection groups = getGroupCollection();
		return ImmutableList.copyOf(Iterables.transform(Iterables.filter(getHandle().getParents(), new Predicate<Subject>() {
			@Override
			public boolean apply(Subject input) {
				return input.getContainingCollection().equals(groups);
			}
		}), new Function<Subject, String>() {
			@Nullable
			@Override
			public String apply(Subject input) {
				return input.getIdentifier();
			}
		}));
	}

	@Override
	public boolean inGroup(String groupName) {
		return getHandle().isChildOf(getGroupCollection().get(groupName));
	}

	private SubjectCollection getGroupCollection() {
		return CommandHelperSponge.self.theGame.getServiceManager().provideUnchecked(PermissionService.class).getGroupSubjects();
	}

	@Override
	public CommandSource getHandle() {
		return source;
	}
}
