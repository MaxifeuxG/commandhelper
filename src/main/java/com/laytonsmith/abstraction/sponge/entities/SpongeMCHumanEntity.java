package com.laytonsmith.abstraction.sponge.entities;

import com.laytonsmith.PureUtilities.Vector3D;
import com.laytonsmith.abstraction.MCHumanEntity;
import com.laytonsmith.abstraction.MCInventory;
import com.laytonsmith.abstraction.MCInventoryView;
import com.laytonsmith.abstraction.MCItemStack;
import com.laytonsmith.abstraction.MCLocation;
import com.laytonsmith.abstraction.MCProjectile;
import com.laytonsmith.abstraction.MCProjectileSource;
import com.laytonsmith.abstraction.enums.MCGameMode;
import com.laytonsmith.abstraction.enums.MCProjectileType;
import com.laytonsmith.abstraction.sponge.SpongeMCItemStack;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Human;

/**
 * SpongeMCHuman, 6/5/2015 11:06 PM
 *
 * @author jb_aero
 */
public class SpongeMCHumanEntity extends SpongeMCLivingEntity implements MCHumanEntity, MCProjectileSource {

	final Human human;

	public SpongeMCHumanEntity(Entity param) {
		super(param);
		human = (Human) param;
	}

	@Override
	public Human getHandle() {
		return human;
	}

	@Override
	public void closeInventory() {
		getHandle().closeInventory();
	}

	@Override
	public MCGameMode getGameMode() {
		return null;
	}

	@Override
	public MCItemStack getItemInHand() {
		return getHandle().getItemInHand().isPresent()
				? new SpongeMCItemStack(getHandle().getItemInHand().get())
				: null;
	}

	@Override
	public MCItemStack getItemOnCursor() {
		return null;
	}

	@Override
	public String getName() {
		return getHandle().getName();
	}

	@Override
	public int getSleepTicks() {
		return 0;
	}

	@Override
	public boolean isBlocking() {
		return false;
	}

	@Override
	public boolean isSleeping() {
		return false;
	}

	@Override
	public MCInventoryView openEnchanting(MCLocation location, boolean force) {
		return null;
	}

	@Override
	public MCInventoryView openInventory(MCInventory inventory) {
		return null;
	}

	@Override
	public MCInventoryView getOpenInventory() {
		return null;
	}

	@Override
	public MCInventory getEnderChest() {
		return null;
	}

	@Override
	public MCInventoryView openWorkbench(MCLocation loc, boolean force) {
		return null;
	}

	@Override
	public void setGameMode(MCGameMode mode) {

	}

	@Override
	public void setItemInHand(MCItemStack item) {

	}

	@Override
	public void setItemOnCursor(MCItemStack item) {

	}

	@Override
	public MCInventory getInventory() {
		return null;
	}

	@Override
	public MCProjectile launchProjectile(MCProjectileType projectile) {
		return null;
	}

	@Override
	public MCProjectile launchProjectile(MCProjectileType projectile, Vector3D init) {
		return null;
	}
}
