package com.laytonsmith.abstraction.bukkit;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.laytonsmith.abstraction.MCLivingEntity;
import com.laytonsmith.abstraction.MCPotionEffect;
import com.laytonsmith.abstraction.MCPotionType;

public class BukkitMCPotionEffect implements MCPotionEffect {

	PotionEffect pe;
	public BukkitMCPotionEffect(PotionEffect effect) {
		pe = effect;
	}
	
	public BukkitMCPotionEffect(PotionEffectType type, int duration, int amplifier, boolean ambient) {
		this(new PotionEffect(type, duration, amplifier, ambient));
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
