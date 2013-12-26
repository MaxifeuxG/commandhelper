package com.laytonsmith.abstraction.bukkit;

import com.laytonsmith.abstraction.AbstractionObject;
import com.laytonsmith.abstraction.MCBlockCommandSender;
import com.laytonsmith.abstraction.blocks.MCBlock;
import com.laytonsmith.abstraction.bukkit.blocks.BukkitMCBlock;
import com.laytonsmith.core.Static;

import org.bukkit.command.BlockCommandSender;

/**
 *
 * @author Layton
 */
public class BukkitMCBlockCommandSender extends BukkitMCCommandSender implements MCBlockCommandSender {

	BlockCommandSender bcs;
	public BukkitMCBlockCommandSender(AbstractionObject a){
        this((BlockCommandSender)null);
        if(a instanceof MCBlockCommandSender){
            this.c = ((BlockCommandSender)a.getHandle());
        } else {
            throw new ClassCastException();
        }
	}
	
	public BukkitMCBlockCommandSender(BlockCommandSender bcs){
		super(bcs);
		this.bcs = bcs;
	}
	
	public MCBlock getBlock() {
		return new BukkitMCBlock(bcs.getBlock());
	}

	@Override
	public String getName() {
		return Static.getBlockPrefix() + bcs.getName();
	}
}
