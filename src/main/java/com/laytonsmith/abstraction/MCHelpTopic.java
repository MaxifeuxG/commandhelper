package com.laytonsmith.abstraction;

public interface MCHelpTopic extends AbstractionObject {
	
	public String getName();
	
	public String getShortText();
	
	public String getFullText(MCCommandSender user);
	
	public void setShortText(String text);
	
	public void setFullText(String text);
	
	public void setViewPermission(String permission);
	
	public boolean canSee(MCCommandSender user);
}
