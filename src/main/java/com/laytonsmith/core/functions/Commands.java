package com.laytonsmith.core.functions;

import java.util.HashMap;
import java.util.Map;

import com.laytonsmith.core.constructs.CClosure;

public class Commands {

	public static String docs() {
		return "A series of functions for creating and managing custom commands.";
	}
	
	public static Map<String, CClosure> onCommand = new HashMap<String, CClosure>();
	public static Map<String, CClosure> onTabComplete = new HashMap<String, CClosure>();
}
