package com.laytonsmith.abstraction.sponge.entities;

import com.google.common.base.Optional;
import com.laytonsmith.abstraction.MCEntity;
import com.laytonsmith.abstraction.MCLocation;
import com.laytonsmith.abstraction.MCMetadataValue;
import com.laytonsmith.abstraction.MCPlugin;
import com.laytonsmith.abstraction.MCServer;
import com.laytonsmith.abstraction.MCWorld;
import com.laytonsmith.abstraction.MVector3D;
import com.laytonsmith.abstraction.enums.MCDamageCause;
import com.laytonsmith.abstraction.enums.MCEntityEffect;
import com.laytonsmith.abstraction.enums.MCEntityType;
import com.laytonsmith.abstraction.enums.MCTeleportCause;
import com.laytonsmith.abstraction.events.MCEntityDamageEvent;
import com.laytonsmith.abstraction.sponge.SpongeMCLocation;
import org.spongepowered.api.data.manipulators.DisplayNameData;
import org.spongepowered.api.data.manipulators.catalogs.CatalogEntityData;
import org.spongepowered.api.data.manipulators.entities.VehicleData;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.world.Location;

import java.util.List;
import java.util.UUID;

/**
 * SpongeMCEntity, 5/11/2015 4:13 PM
 *
 * @author jb_aero
 */
public class SpongeMCEntity implements MCEntity {

	private Entity entity;

	public SpongeMCEntity(Entity param) {
		entity = param;
	}

	@Override
	public boolean eject() {
		Optional<VehicleData> vd = getHandle().getData(CatalogEntityData.VEHICLE_DATA);
		if (vd.isPresent()) {
			vd.get().setPassenger(null);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void fireEntityDamageEvent(MCDamageCause dc) {

	}

	@Override
	public int getEntityId() {
		return 0;
	}

	@Override
	public float getFallDistance() {
		return 0;
	}

	@Override
	public int getFireTicks() {
		return 0;
	}

	@Override
	public MCEntityDamageEvent getLastDamageCause() {
		return null;
	}

	@Override
	public MCLocation getLocation() {
		return new SpongeMCLocation(getHandle().getLocation());
	}

	@Override
	public MCLocation asyncGetLocation() {
		return null;
	}

	@Override
	public int getMaxFireTicks() {
		return 0;
	}

	@Override
	public List<MCEntity> getNearbyEntities(double x, double y, double z) {
		return null;
	}

	@Override
	public MCEntity getPassenger() {
		return null;
	}

	@Override
	public MCServer getServer() {
		return null;
	}

	@Override
	public int getTicksLived() {
		return 0;
	}

	@Override
	public MCEntityType getType() {
		return null;
	}

	@Override
	public UUID getUniqueId() {
		return null;
	}

	@Override
	public MCEntity getVehicle() {
		return null;
	}

	@Override
	public MVector3D getVelocity() {
		return null;
	}

	@Override
	public void setVelocity(MVector3D v) {

	}

	@Override
	public MCWorld getWorld() {
		return null;
	}

	@Override
	public boolean isDead() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean isInsideVehicle() {
		return false;
	}

	@Override
	public boolean isOnGround() {
		return false;
	}

	@Override
	public boolean leaveVehicle() {
		return false;
	}

	@Override
	public void playEffect(MCEntityEffect type) {

	}

	@Override
	public void remove() {

	}

	@Override
	public void setFallDistance(float distance) {

	}

	@Override
	public void setFireTicks(int ticks) {

	}

	@Override
	public void setLastDamageCause(MCEntityDamageEvent event) {

	}

	@Override
	public boolean setPassenger(MCEntity passenger) {
		return false;
	}

	@Override
	public void setTicksLived(int value) {

	}

	@Override
	public boolean teleport(MCEntity destination) {
		getHandle().setLocation((Location) destination.getLocation().getHandle());
		return true;
	}

	@Override
	public boolean teleport(MCEntity destination, MCTeleportCause cause) {
		return false;
	}

	@Override
	public boolean teleport(MCLocation location) {
		return false;
	}

	@Override
	public boolean teleport(MCLocation location, MCTeleportCause cause) {
		return false;
	}

	@Override
	public void setCustomName(String name) {

	}

	@Override
	public String getCustomName() {
		DisplayNameData name = getHandle().getData(CatalogEntityData.DISPLAY_NAME_DATA).orNull();
		return name == null ? null : name.getDisplayName().toString();
	}

	@Override
	public void setCustomNameVisible(boolean visible) {

	}

	@Override
	public boolean isCustomNameVisible() {
		return false;
	}

	@Override
	public List<MCMetadataValue> getMetadata(String metadataKey) {
		return null;
	}

	@Override
	public boolean hasMetadata(String metadataKey) {
		return false;
	}

	@Override
	public void removeMetadata(String metadataKey, MCPlugin owningPlugin) {

	}

	@Override
	public void setMetadata(String metadataKey, MCMetadataValue newMetadataValue) {

	}

	@Override
	public Entity getHandle() {
		return entity;
	}
}
