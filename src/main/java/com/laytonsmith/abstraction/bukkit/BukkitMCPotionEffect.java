package com.laytonsmith.abstraction.bukkit;

import org.bukkit.potion.PotionEffect;

import com.laytonsmith.abstraction.MCLivingEntity;
import com.laytonsmith.abstraction.MCPotionEffect;
import com.laytonsmith.abstraction.enums.MCPotionType;

public class BukkitMCPotionEffect implements MCPotionEffect {

	PotionEffect pe;
	public BukkitMCPotionEffect(PotionEffect effect) {
		pe = effect;
	}
	
	public boolean apply(MCLivingEntity entity) {
		return pe.apply(((BukkitMCLivingEntity) entity).asLivingEntity());
	}

	public int getAmplifier() {
		return pe.getAmplifier();
	}

	public int getDuration() {
		return pe.getDuration();
	}

	public MCPotionType getType() {
		return BukkitMCPotionType.getEffectType(pe.getType());
	}

	public boolean isAmbient() {
		return pe.isAmbient();
	}

	@Override
	public Object getHandle() {
		return pe;
	}
}
