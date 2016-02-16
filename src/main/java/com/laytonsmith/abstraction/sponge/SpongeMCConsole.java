package com.laytonsmith.abstraction.sponge;

import com.laytonsmith.abstraction.MCConsoleCommandSender;
import org.spongepowered.api.command.source.ConsoleSource;

/**
 * SpongeMCConsole, 6/8/2015 3:46 AM
 *
 * @author jb_aero
 */
public class SpongeMCConsole extends SpongeMCCommandSender implements MCConsoleCommandSender {

	final ConsoleSource console;

	public SpongeMCConsole(ConsoleSource console) {
		super(console);
		this.console = console;
	}
}
