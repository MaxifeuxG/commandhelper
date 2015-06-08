package com.laytonsmith.abstraction.sponge.events;

import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.abstraction.events.MCConsoleCommandEvent;
import com.laytonsmith.abstraction.events.MCPlayerCommandEvent;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.message.CommandEvent;
import org.spongepowered.api.util.event.callback.EventCallback;

/**
 * SpongeMiscEvents, 6/7/2015 8:37 PM
 *
 * @author jb_aero
 */
public class SpongeMiscEvents {

	public static class SpongeMCCommandEvent implements MCPlayerCommandEvent, MCConsoleCommandEvent {

		final CommandEvent event;

		public SpongeMCCommandEvent(CommandEvent event) {
			this.event = event;
		}

		@Override
		public String getCommand() {
			return event.getCommand();
		}

		@Override
		public void cancel() {

		}

		@Override
		public void setCommand(String val) {
			for (EventCallback ec : event.getCallbacks()) {
				if (ec.isBaseGame() && ec instanceof Cancellable) {
					((Cancellable) ec).setCancelled(true);
				}
			}
			event.getCallbacks().add(new EventCallback() {
				@Override
				public boolean isBaseGame() {
					return false;
				}

				@Override
				public Order getOrder() {
					return Order.DEFAULT;
				}

				@Override
				public void run() {
					event.getGame().getCommandDispatcher().process(event.getSource(),
							event.getCommand() + " " + event.getArguments());
				}
			});
		}

		@Override
		public boolean isCancelled() {
			return false;
		}

		@Override
		public MCPlayer getPlayer() {
			return null;
		}

		@Override
		public CommandEvent _GetObject() {
			return event;
		}
	}
}
