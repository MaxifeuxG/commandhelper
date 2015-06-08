package com.laytonsmith.abstraction.sponge;

import com.laytonsmith.abstraction.pluginmessages.MCMessenger;
import com.laytonsmith.abstraction.pluginmessages.MCPluginMessageListenerRegistration;

import java.util.Set;

/**
 * SpongeMCMessenger, 6/5/2015 9:18 PM
 *
 * @author jb_aero
 */
public class SpongeMCMessenger implements MCMessenger {

	@Override
	public MCPluginMessageListenerRegistration registerIncomingPluginChannel(String channel) {
		return null;
	}

	@Override
	public boolean isIncomingChannelRegistered(String channel) {
		return false;
	}

	@Override
	public void unregisterIncomingPluginChannel(String channel) {

	}

	@Override
	public Set<String> getIncomingChannels() {
		return null;
	}

	@Override
	public void closeAllChannels() {

	}
}
