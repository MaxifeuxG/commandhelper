package com.laytonsmith.abstraction;

/**
 * MCEventManager.java, 4/8/2015 12:41 AM
 *
 * @author jb_aero
 */
public interface MCEventManager extends AbstractionObject {

	boolean callEvent(Object event);

	void registerEvents(Object listener);

	void registerEvents(MCPlugin pl, Object listener);

	void registerEventsDynamic(Object listener);

	void unregisterEvents(Object reciever);

	// WIP
	void registerEvent(Object listener);

	void unregisterEvent(Object event);

}
