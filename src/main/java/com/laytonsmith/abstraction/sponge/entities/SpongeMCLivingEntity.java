package com.laytonsmith.abstraction.sponge.entities;

import com.laytonsmith.abstraction.MCEntity;
import com.laytonsmith.abstraction.MCEntityEquipment;
import com.laytonsmith.abstraction.MCLivingEntity;
import com.laytonsmith.abstraction.MCLocation;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.abstraction.blocks.MCBlock;
import com.laytonsmith.core.constructs.Target;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;

import java.util.HashSet;
import java.util.List;

/**
 * SpongeMCLiving, 6/5/2015 11:07 PM
 *
 * @author jb_aero
 */
public class SpongeMCLivingEntity extends SpongeMCEntity implements MCLivingEntity {

	final Living living;

	public SpongeMCLivingEntity(Entity param) {
		super(param);
		living = (Living) param;
	}

	@Override
	public void addEffect(int potionID, int strength, int seconds, boolean ambient, Target t) {

	}

	@Override
	public boolean removeEffect(int potionID) {
		return false;
	}

	@Override
	public int getMaxEffect() {
		return 0;
	}

	@Override
	public List<MCEffect> getEffects() {
		return null;
	}

	@Override
	public void damage(double amount) {

	}

	@Override
	public void damage(double amount, MCEntity source) {

	}

	@Override
	public boolean getCanPickupItems() {
		return false;
	}

	@Override
	public boolean getRemoveWhenFarAway() {
		return false;
	}

	@Override
	public MCEntityEquipment getEquipment() {
		return null;
	}

	@Override
	public double getEyeHeight() {
		return 0;
	}

	@Override
	public double getEyeHeight(boolean ignoreSneaking) {
		return 0;
	}

	@Override
	public MCLocation getEyeLocation() {
		return null;
	}

	@Override
	public double getHealth() {
		return 0;
	}

	@Override
	public MCPlayer getKiller() {
		return null;
	}

	@Override
	public double getLastDamage() {
		return 0;
	}

	@Override
	public MCEntity getLeashHolder() {
		return null;
	}

	@Override
	public MCLivingEntity getTarget(Target t) {
		return null;
	}

	@Override
	public MCBlock getTargetBlock(HashSet<Short> transparent, int maxDistance, boolean castToByte) {
		return null;
	}

	@Override
	public MCBlock getTargetBlock(HashSet<Byte> transparent, int maxDistance) {
		return null;
	}

	@Override
	public List<MCBlock> getLastTwoTargetBlocks(HashSet<Byte> transparent, int maxDistance) {
		return null;
	}

	@Override
	public List<MCBlock> getLineOfSight(HashSet<Byte> transparent, int maxDistance) {
		return null;
	}

	@Override
	public boolean hasLineOfSight(MCEntity other) {
		return false;
	}

	@Override
	public double getMaxHealth() {
		return 0;
	}

	@Override
	public int getMaximumAir() {
		return 0;
	}

	@Override
	public int getMaximumNoDamageTicks() {
		return 0;
	}

	@Override
	public int getNoDamageTicks() {
		return 0;
	}

	@Override
	public int getRemainingAir() {
		return 0;
	}

	@Override
	public boolean isLeashed() {
		return false;
	}

	@Override
	public void resetMaxHealth() {

	}

	@Override
	public void setCanPickupItems(boolean pickup) {

	}

	@Override
	public void setRemoveWhenFarAway(boolean remove) {

	}

	@Override
	public void setHealth(double health) {

	}

	@Override
	public void setLastDamage(double damage) {

	}

	@Override
	public void setLeashHolder(MCEntity holder) {

	}

	@Override
	public void setMaxHealth(double health) {

	}

	@Override
	public void setMaximumAir(int ticks) {

	}

	@Override
	public void setMaximumNoDamageTicks(int ticks) {

	}

	@Override
	public void setNoDamageTicks(int ticks) {

	}

	@Override
	public void setRemainingAir(int ticks) {

	}

	@Override
	public void setTarget(MCLivingEntity target, Target t) {

	}

	@Override
	public void kill() {

	}
}
