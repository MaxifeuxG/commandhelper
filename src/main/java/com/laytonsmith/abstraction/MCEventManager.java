package com.laytonsmith.abstraction;

/**
 * Created by jb_aero on 4/8/2015.
 */
public interface MCEventManager {

	boolean callEvent(Object event);

	void registerEvents(Object listener);

	void registerEvents(MCPlugin pl, Object listener);

	void registerEventsDynamic(Object listener);

	void unregisterEvents(Object reciever);

	// WIP
	void registerEvent(Object listener);

	void unregisterEvent(Object event);

}
