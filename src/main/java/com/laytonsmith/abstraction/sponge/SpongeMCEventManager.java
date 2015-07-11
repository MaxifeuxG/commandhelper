package com.laytonsmith.abstraction.sponge;

import com.laytonsmith.abstraction.MCEventManager;
import com.laytonsmith.abstraction.MCPlugin;
import org.spongepowered.api.service.event.EventManager;

/**
 * SpongeMCEventManager, 7/11/2015 12:38 AM
 *
 * @author jb_aero
 */
public class SpongeMCEventManager implements MCEventManager {

	final EventManager eventManager;

	public SpongeMCEventManager(EventManager eventManager) {
		this.eventManager = eventManager;
	}

	@Override
	public EventManager getHandle() {
		return eventManager;
	}

	@Override
	public boolean callEvent(Object event) {
		return false;
	}

	@Override
	public void registerEvents(Object listener) {

	}

	@Override
	public void registerEvents(MCPlugin pl, Object listener) {

	}

	@Override
	public void registerEventsDynamic(Object listener) {

	}

	@Override
	public void unregisterEvents(Object reciever) {

	}

	@Override
	public void registerEvent(Object listener) {

	}

	@Override
	public void unregisterEvent(Object event) {

	}
}
