package com.laytonsmith.abstraction.sponge;

import com.google.common.collect.ImmutableSet;
import com.laytonsmith.abstraction.pluginmessages.MCMessenger;
import com.laytonsmith.abstraction.pluginmessages.MCPluginMessageListenerRegistration;
import org.spongepowered.api.Platform;
import org.spongepowered.api.network.ChannelRegistrar;

import java.util.Set;

/**
 * SpongeMCMessenger, 6/5/2015 9:18 PM
 *
 * @author jb_aero
 */
public class SpongeMCMessenger implements MCMessenger {
	private final ChannelRegistrar registrar;

	public SpongeMCMessenger(ChannelRegistrar registrar) {
		this.registrar = registrar;
	}

	@Override
	public MCPluginMessageListenerRegistration registerIncomingPluginChannel(String channel) {
		//registrar.registerChannel();
		return null;
	}

	@Override
	public boolean isIncomingChannelRegistered(String channel) {
		return registrar.getRegisteredChannels(Platform.Type.SERVER).contains(channel);
	}

	@Override
	public void unregisterIncomingPluginChannel(String channel) {
		// TODOO: is this exposed in API?
	}

	@Override
	public Set<String> getIncomingChannels() {
		return ImmutableSet.copyOf(registrar.getRegisteredChannels(Platform.Type.SERVER));
	}

	@Override
	public void closeAllChannels() {
	}
}
