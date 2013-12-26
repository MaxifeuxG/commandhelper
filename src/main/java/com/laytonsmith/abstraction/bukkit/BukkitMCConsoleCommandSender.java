

package com.laytonsmith.abstraction.bukkit;

import com.laytonsmith.abstraction.MCConsoleCommandSender;
import com.laytonsmith.core.Static;

import org.bukkit.command.ConsoleCommandSender;

/**
 *
 * @author layton
 */
public class BukkitMCConsoleCommandSender extends BukkitMCCommandSender implements MCConsoleCommandSender{

    ConsoleCommandSender ccs;
    public BukkitMCConsoleCommandSender(ConsoleCommandSender ccs){
        super(ccs);
        this.ccs = ccs;
    }
    
    @Override
    public String getName() {
    	return Static.getConsoleName();
    }
}
