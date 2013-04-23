package com.laytonsmith.abstraction.enums;

import com.laytonsmith.abstraction.MCPotionEffect;
import com.laytonsmith.abstraction.StaticLayer;

/**
 * 
 * @author jb_aero
 */
public interface MCPotionType {
	
	public static final MCPotionType BLINDNESS = StaticLayer.GetConvertor().getPotionEffectType(1);
	public static final MCPotionType CONFUSION = StaticLayer.GetConvertor().getPotionEffectType(2);
	public static final MCPotionType DAMAGE_RESISTANCE = StaticLayer.GetConvertor().getPotionEffectType(3);
	public static final MCPotionType FAST_DIGGING = StaticLayer.GetConvertor().getPotionEffectType(4);
	public static final MCPotionType FIRE_RESISTANCE = StaticLayer.GetConvertor().getPotionEffectType(5);
	public static final MCPotionType HARM = StaticLayer.GetConvertor().getPotionEffectType(6);
	public static final MCPotionType HEAL = StaticLayer.GetConvertor().getPotionEffectType(7);
	public static final MCPotionType HUNGER = StaticLayer.GetConvertor().getPotionEffectType(8);
	public static final MCPotionType INCREASE_DAMAGE = StaticLayer.GetConvertor().getPotionEffectType(9);
	public static final MCPotionType INVISIBILITY = StaticLayer.GetConvertor().getPotionEffectType(10);
	public static final MCPotionType JUMP = StaticLayer.GetConvertor().getPotionEffectType(11);
	public static final MCPotionType NIGHT_VISION = StaticLayer.GetConvertor().getPotionEffectType(12);
	public static final MCPotionType POISON = StaticLayer.GetConvertor().getPotionEffectType(13);
	public static final MCPotionType REGENERATION = StaticLayer.GetConvertor().getPotionEffectType(14);
	public static final MCPotionType SLOW = StaticLayer.GetConvertor().getPotionEffectType(15);
	public static final MCPotionType SLOW_DIGGING = StaticLayer.GetConvertor().getPotionEffectType(16);
	public static final MCPotionType SPEED = StaticLayer.GetConvertor().getPotionEffectType(17);
	public static final MCPotionType WATER_BREATHING = StaticLayer.GetConvertor().getPotionEffectType(18);
	public static final MCPotionType WEAKNESS = StaticLayer.GetConvertor().getPotionEffectType(19);
	public static final MCPotionType WITHER = StaticLayer.GetConvertor().getPotionEffectType(20);
	
	public double getDurationMultiplier();
	public int getID();
	public String getName();
	public boolean isInstant();
	public String toString();
	public int hashCode();
	
	public MCPotionEffect createEffect(int duration, int amplifier);
}
