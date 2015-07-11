package com.laytonsmith.abstraction.sponge;

import com.laytonsmith.abstraction.MCEnchantment;
import com.laytonsmith.abstraction.MCItemMeta;
import com.laytonsmith.abstraction.MCItemStack;
import com.laytonsmith.abstraction.MCMaterialData;
import com.laytonsmith.abstraction.blocks.MCMaterial;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Map;

/**
 * SpongeMCItemStack, 7/8/2015 11:27 PM
 *
 * @author jb_aero
 */
public class SpongeMCItemStack implements MCItemStack {

	final ItemStack stack;

	public SpongeMCItemStack(ItemStack itemStack) {
		stack = itemStack;
	}

	@Override
	public ItemStack getHandle() {
		return stack;
	}

	@Override
	public MCMaterialData getData() {
		return null;
	}

	@Override
	public short getDurability() {
		return 0;
	}

	@Override
	public int getTypeId() {
		return 0;
	}

	@Override
	public void setDurability(short data) {

	}

	@Override
	public void addEnchantment(MCEnchantment e, int level) {

	}

	@Override
	public void addUnsafeEnchantment(MCEnchantment e, int level) {

	}

	@Override
	public Map<MCEnchantment, Integer> getEnchantments() {
		return null;
	}

	@Override
	public void removeEnchantment(MCEnchantment e) {

	}

	@Override
	public MCMaterial getType() {
		return null;
	}

	@Override
	public void setTypeId(int type) {

	}

	@Override
	public int maxStackSize() {
		return getHandle().getMaxStackQuantity();
	}

	@Override
	public int getAmount() {
		return getHandle().getQuantity();
	}

	@Override
	public void setData(int data) {

	}

	@Override
	public boolean hasItemMeta() {
		return false;
	}

	@Override
	public MCItemMeta getItemMeta() {
		return null;
	}

	@Override
	public void setItemMeta(MCItemMeta im) {

	}
}
