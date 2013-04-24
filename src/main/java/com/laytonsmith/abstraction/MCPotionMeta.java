package com.laytonsmith.abstraction;

import java.util.List;

public interface MCPotionMeta extends MCItemMeta {

	public boolean addCustomEffect(MCPotionEffect effect);
	public boolean clearCustomEffects();
	public List<MCPotionEffect> getCustomEffects();
	public boolean hasCustomEffect(int id);
	public boolean hasCustomEffects();
	public boolean removeCustomEffect(int id);
	public boolean setMainEffect(int id);
}
