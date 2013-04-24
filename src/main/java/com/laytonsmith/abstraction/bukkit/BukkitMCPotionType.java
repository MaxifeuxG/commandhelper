package com.laytonsmith.abstraction.bukkit;

import org.bukkit.potion.PotionEffectType;

import com.laytonsmith.abstraction.MCPotionEffect;
import com.laytonsmith.abstraction.MCPotionType;

/**
 * 
 * @author jb_aero
 */
public class BukkitMCPotionType implements MCPotionType {

	PotionEffectType type;
	
	public BukkitMCPotionType(PotionEffectType effect) {
		type = effect;
	}
	
	public static MCPotionType getEffectType(PotionEffectType concrete) {
		return new BukkitMCPotionType(concrete);
	}
	
	public static PotionEffectType getEffectType(MCPotionType abs) {
		return ((BukkitMCPotionType) abs).getEffectType();
	}
	
	public PotionEffectType getEffectType() {
		return type;
	}

	public boolean isInstant() {
		return type.isInstant();
	}

	public MCPotionEffect createEffect(int duration, int amplifier, boolean ambient) {
		return new BukkitMCPotionEffect(type, duration, amplifier, ambient);
	}

	public double getDurationMultiplier() {
		return type.getDurationModifier();
	}

	public int getID() {
		return type.getId();
	}

	public String getName() {
		return type.getName();
	}
	
	@Override
	public String toString() {
		return type.toString();
	}
	
	@Override
	public int hashCode() {
		return type.hashCode();
	}
}
