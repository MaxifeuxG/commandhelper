package com.laytonsmith.abstraction.bukkit;

import org.bukkit.help.HelpTopic;

import com.laytonsmith.abstraction.MCCommandSender;
import com.laytonsmith.abstraction.MCHelpTopic;

public class BukkitMCHelpTopic implements MCHelpTopic {

	HelpTopic topic;
	public BukkitMCHelpTopic(HelpTopic bht) {
		topic = bht;
	}

	@Override
	public Object getHandle() {
		return topic;
	}

	@Override
	public String getName() {
		return topic.getName();
	}

	@Override
	public String getShortText() {
		return topic.getShortText();
	}

	@Override
	public String getFullText(MCCommandSender user) {
		return topic.getFullText(((BukkitMCCommandSender) user)._CommandSender());
	}

	@Override
	public void setShortText(String text) {
		topic.amendTopic(text, null);
	}

	@Override
	public void setFullText(String text) {
		topic.amendTopic(null, text);
	}

	@Override
	public void setViewPermission(String permission) {
		topic.amendCanSee(permission);
	}

	@Override
	public boolean canSee(MCCommandSender user) {
		return topic.canSee(((BukkitMCCommandSender) user)._CommandSender());
	}
}
