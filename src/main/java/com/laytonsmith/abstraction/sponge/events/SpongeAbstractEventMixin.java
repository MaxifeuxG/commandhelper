package com.laytonsmith.abstraction.sponge.events;

import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.events.AbstractEvent;
import com.laytonsmith.core.events.BindableEvent;
import com.laytonsmith.core.events.EventMixinInterface;
import com.laytonsmith.core.exceptions.EventException;

import java.util.HashMap;
import java.util.Map;

/**
 * SpongeAbstractEventMixin, 6/5/2015 7:30 PM
 *
 * @author jb_aero
 */
public class SpongeAbstractEventMixin implements EventMixinInterface {

	AbstractEvent mySuper;

	public SpongeAbstractEventMixin(AbstractEvent mySuper) {
		this.mySuper = mySuper;
	}

	@Override
	public void cancel(BindableEvent e, boolean state) {

	}

	@Override
	public boolean isCancellable(BindableEvent o) {
		return false;
	}

	@Override
	public Map<String, Construct> evaluate_helper(BindableEvent e) throws EventException {
		return new HashMap<String, Construct>();
	}

	@Override
	public void manualTrigger(BindableEvent e) {

	}

	@Override
	public boolean isCancelled(BindableEvent o) {
		return false;
	}
}
