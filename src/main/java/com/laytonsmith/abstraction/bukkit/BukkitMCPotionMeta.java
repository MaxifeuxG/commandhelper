package com.laytonsmith.abstraction.bukkit;

import com.laytonsmith.abstraction.MCPotionEffect;
import com.laytonsmith.abstraction.MCPotionMeta;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BukkitMCPotionMeta extends BukkitMCItemMeta implements MCPotionMeta {

	PotionMeta pm;
	public BukkitMCPotionMeta(PotionMeta pomet) {
		super(pomet);
		pm = pomet;
	}
	
	public boolean addCustomEffect(MCPotionEffect effect) {
		return pm.addCustomEffect((PotionEffect) effect.getHandle(), true);
	}

	public boolean clearCustomEffects() {
		return pm.clearCustomEffects();
	}

	public List<MCPotionEffect> getCustomEffects() {
		List<MCPotionEffect> list = new ArrayList<MCPotionEffect>();
		for (PotionEffect pe : pm.getCustomEffects()) {
			list.add(new BukkitMCPotionEffect(pe));
		}
		return list;
	}

	public boolean hasCustomEffect(int id) {
		return pm.hasCustomEffect(PotionEffectType.getById(id));
	}

	public boolean hasCustomEffects() {
		return pm.hasCustomEffects();
	}

	public boolean removeCustomEffect(int id) {
		return pm.removeCustomEffect(PotionEffectType.getById(id));
	}

	public boolean setMainEffect(int id) {
		return pm.setMainEffect(PotionEffectType.getById(id));
	}

}
