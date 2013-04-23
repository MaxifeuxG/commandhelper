package com.laytonsmith.abstraction;

import com.laytonsmith.abstraction.enums.MCPotionType;

public interface MCPotionEffect extends AbstractionObject {

	public boolean apply(MCLivingEntity entity);
	
	public int getAmplifier();
	
	public int getDuration();
	
	public MCPotionType getType();
	
	public boolean isAmbient();
}
