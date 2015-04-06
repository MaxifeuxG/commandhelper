

package com.laytonsmith.abstraction.bukkit;

import com.laytonsmith.PureUtilities.Common.ReflectionUtils;
import com.laytonsmith.abstraction.AbstractionObject;
import com.laytonsmith.abstraction.MCEventManager;
import com.laytonsmith.abstraction.MCPlugin;
import com.laytonsmith.abstraction.MCPluginManager;
import com.laytonsmith.annotations.EventIdentifier;
import com.laytonsmith.commandhelper.CommandHelperPlugin;
import com.laytonsmith.core.CHLog;
import com.laytonsmith.core.constructs.Target;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.TimedRegisteredListener;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 
 */
public class BukkitMCPluginManager implements MCPluginManager, MCEventManager {

    PluginManager p;
    public BukkitMCPluginManager(PluginManager pluginManager) {
        this.p = pluginManager;
    }
    
    public BukkitMCPluginManager(AbstractionObject a){
        this((PluginManager)null);
        if(a instanceof MCPluginManager){
            this.p = ((PluginManager)a.getHandle());
        } else {
            throw new ClassCastException();
        }
    }
    
	@Override
    public PluginManager getHandle(){
        return p;
    }

	@Override
    public MCPlugin getPlugin(String name) {
        if(p.getPlugin(name) == null){
            return null;
        }
        return new BukkitMCPlugin(p.getPlugin(name));
    }
    
    public PluginManager __PluginManager(){
        return p;
    }

	@Override
	public String toString() {
		return p.toString();
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof BukkitMCPluginManager?p.equals(((BukkitMCPluginManager)obj).p):false);
	}

	@Override
	public int hashCode() {
		return p.hashCode();
	}

	@Override
	public List<MCPlugin> getPlugins() {
		List<MCPlugin> retn = new ArrayList<MCPlugin>();
		Plugin[] plugs = p.getPlugins();
		
		for (Plugin plug : plugs) {
			retn.add(new BukkitMCPlugin(plug));
		}
		
		return retn;
	}

    @Override
    public boolean isLoaded(String id) {
        return p.isPluginEnabled(id);
    }

    @Override
    public boolean callEvent(Object event) {
        return false;
    }

	/**
	 * Register all events for one of our Listener classes.
	 *
	 * @param listener
	 */
	@Override
	public void registerEvents(Object listener) {
		p.registerEvents((Listener) listener, CommandHelperPlugin.self);
	}

	/**
	 * Register all events in a Listener class.
	 *
	 * @param listener
	 */
	@Override
	public void registerEvents(MCPlugin pl, Object listener) {
		p.registerEvents((Listener) listener, ((BukkitMCPlugin) pl).getHandle());
	}

    /**
     * This method is based on Bukkit's JavaPluginLoader:createRegisteredListeners Part of this code would be run
     * normally using the other register method
     */
    @Override
    public void registerEventsDynamic(Object listener) {
        for (final java.lang.reflect.Method method : listener.getClass().getMethods()) {
            EventIdentifier identifier = method.getAnnotation(EventIdentifier.class);
            EventHandler defaultHandler = method.getAnnotation(EventHandler.class);
            EventPriority priority = EventPriority.LOWEST;
            if (identifier == null || !identifier.event().existsInCurrent()) {
                continue;
            }
            if (defaultHandler != null) {
                priority = defaultHandler.priority();
            }
            Class<? extends Event> eventClass = null;
            try {
                eventClass = (Class<? extends Event>) Class.forName(identifier.className());
            } catch (ClassNotFoundException | ClassCastException e) {
                CHLog.GetLogger().e(CHLog.Tags.RUNTIME, "Could not listen for " + identifier.event().name()
                                + " because the class " + identifier.className() + " could not be found."
                                + " This problem is not expected to occur, so please report it on the bug tracker if it does.",
                        Target.UNKNOWN);
                continue;
            }
			HandlerList handler;
			try {
				handler = (HandlerList) ReflectionUtils.invokeMethod(eventClass, null, "getHandlerList");
			} catch (ReflectionUtils.ReflectionException ref) {
				Class eventSuperClass = eventClass.getSuperclass();
				if (eventSuperClass != null) {
					try {
						handler = (HandlerList) ReflectionUtils.invokeMethod(eventSuperClass, null, "getHandlerList");
					} catch (ReflectionUtils.ReflectionException refInner) {
						CHLog.GetLogger().e(CHLog.Tags.RUNTIME, "Could not listen for " + identifier.event().name()
										+ " because the handler for class " + identifier.className()
										+ " could not be found. An attempt has already been made to find the"
										+ " correct handler, but" + eventSuperClass.getName()
										+ " did not have it either. Please report this on the bug tracker.",
								Target.UNKNOWN);
						continue;
					}
				} else {
					CHLog.GetLogger().e(CHLog.Tags.RUNTIME, "Could not listen for " + identifier.event().name()
									+ " because the handler for class " + identifier.className()
									+ " could not be found. An attempt has already been made to find the"
									+ " correct handler, but no superclass could be found."
									+ " Please report this on the bug tracker.",
							Target.UNKNOWN);
					continue;
				}
			}
            final Class<? extends Event> finalEventClass = eventClass;
            EventExecutor executor = new EventExecutor() {
                @Override
                public void execute(Listener listener, Event event) throws EventException {
                    try {
                        if (!finalEventClass.isAssignableFrom(event.getClass())) {
                            return;
                        }
                        method.invoke(listener, event);
                    } catch (InvocationTargetException ex) {
                        throw new EventException(ex.getCause());
                    } catch (Throwable t) {
                        throw new EventException(t);
                    }
                }
            };
            if (p.useTimings()) {
                handler.register(new TimedRegisteredListener((Listener) listener, executor, priority,
                        CommandHelperPlugin.self, false));
            } else {
                handler.register(new RegisteredListener(((Listener) listener), executor, priority,
                        CommandHelperPlugin.self, false));
            }
        }
    }

    @Override
    public void unregisterEvents(Object receiver) {

    }

    @Override
    public void registerEvent(Object listener) {

    }

    @Override
    public void unregisterEvent(Object event) {

    }
}
