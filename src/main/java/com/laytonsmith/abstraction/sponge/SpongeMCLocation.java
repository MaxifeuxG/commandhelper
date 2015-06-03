package com.laytonsmith.abstraction.sponge;

import com.laytonsmith.PureUtilities.Vector3D;
import com.laytonsmith.abstraction.MCChunk;
import com.laytonsmith.abstraction.MCItemStack;
import com.laytonsmith.abstraction.MCLocation;
import com.laytonsmith.abstraction.MCMetadataValue;
import com.laytonsmith.abstraction.MCPlugin;
import com.laytonsmith.abstraction.MCWorld;
import com.laytonsmith.abstraction.blocks.MCBlock;
import com.laytonsmith.abstraction.blocks.MCBlockFace;
import com.laytonsmith.abstraction.blocks.MCBlockState;
import com.laytonsmith.abstraction.blocks.MCCommandBlock;
import com.laytonsmith.abstraction.blocks.MCMaterial;
import com.laytonsmith.abstraction.blocks.MCSign;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * SpongeMCLocation, 5/11/2015 4:53 PM
 *
 * @author jb_aero
 */
public class SpongeMCLocation implements MCLocation, MCBlock {

	private Location loc;
	private double yaw, pitch;

	public SpongeMCLocation(Location param) {
		this(param, 0, 0);
	}

	public SpongeMCLocation(Location param, float yaw, float pitch) {
		this(param, (double) yaw, (double) pitch);
	}

	public SpongeMCLocation(Location param, double yaw, double pitch) {
		this.loc = param;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	@Override
	public double getX() {
		return getHandle().getX();
	}

	@Override
	public double getY() {
		return getHandle().getY();
	}

	@Override
	public double getZ() {
		return getHandle().getZ();
	}

	@Override
	public MCSign getSign() {
		return null;
	}

	@Override
	public MCLocation getLocation() {
		return this;
	}

	@Override
	public boolean isSign() {
		return getHandle().getType() == BlockTypes.STANDING_SIGN || getHandle().getType() == BlockTypes.WALL_SIGN;
	}

	@Override
	public MCCommandBlock getCommandBlock() {
		return null;
	}

	@Override
	public boolean isCommandBlock() {
		return getHandle().getType() == BlockTypes.COMMAND_BLOCK;
	}

	@Override
	public boolean isNull() {
		return false;
	}

	@Override
	public boolean isSolid() {
		return getHandle().getType().isSolidCube();
	}

	@Override
	public boolean isFlammable() {
		for (Direction d : Direction.values()) {
			if (getHandle().isFaceFlammable(d)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isTransparent() {
		return false;
	}

	@Override
	public boolean isOccluding() {
		return false;
	}

	@Override
	public boolean isBurnable() {
		return isFlammable();
	}

	@Override
	public Collection<MCItemStack> getDrops() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public Collection<MCItemStack> getDrops(MCItemStack tool) {
		return Collections.EMPTY_LIST;
	}

	@Override
	public int getLightLevel() {
		return 0;
	}

	@Override
	public int getBlockPower() {
		return 0;
	}

	@Override
	public boolean isBlockPowered() {
		return getHandle().isPowered();
	}

	@Override
	public MCBlock getRelative(MCBlockFace face) {
		return null;
	}

	@Override
	public MCBlockFace getFace(MCBlock get) {
		return null;
	}

	@Override
	public double distance(MCLocation o) {
		return toVector().length();
	}

	@Override
	public double distanceSquared(MCLocation o) {
		return toVector().lengthSquared();
	}

	@Override
	public int getTypeId() {
		return 0;
	}

	@Override
	public byte getData() {
		return 0;
	}

	@Override
	public void setTypeId(int idata) {

	}

	@Override
	public void setData(byte imeta) {

	}

	@Override
	public void setTypeAndData(int type, byte data, boolean physics) {

	}

	@Override
	public double getTemperature() {
		return 0;
	}

	@Override
	public MCBlockState getState() {
		return null;
	}

	@Override
	public MCMaterial getType() {
		return null;
	}

	@Override
	public MCWorld getWorld() {
		Extent e = getHandle().getExtent();
		if (e instanceof Chunk) {
			return new SpongeMCWorld(((Chunk) e).getWorld());
		}
		return new SpongeMCWorld((World) e);
	}

	@Override
	public float getYaw() {
		return (float) yaw;
	}

	@Override
	public float getPitch() {
		return (float) pitch;
	}

	@Override
	public double getYawD() {
		return yaw;
	}

	@Override
	public double getPitchD() {
		return pitch;
	}

	@Override
	public int getBlockX() {
		return getHandle().getBlockX();
	}

	@Override
	public int getBlockY() {
		return getHandle().getBlockY();
	}

	@Override
	public int getBlockZ() {
		return getHandle().getBlockZ();
	}

	@Override
	public MCChunk getChunk() {
		Extent e = getHandle().getExtent();
		if (e instanceof Chunk) {
			return new SpongeMCChunk((Chunk) e);
		}
		if (e instanceof World) {
			return new SpongeMCChunk(((World) e).loadChunk(getBlockX(), getBlockY(), getBlockZ(), true).get());
		}
		throw new IllegalStateException("This block/location was created with a non-chunk, non-world extent.");
	}

	@Override
	public MCBlock getBlock() {
		return this;
	}

	@Override
	public MCLocation add(MCLocation vec) {
		return null;
	}

	@Override
	public MCLocation add(Vector3D vec) {
		return null;
	}

	@Override
	public MCLocation add(double x, double y, double z) {
		return null;
	}

	@Override
	public MCLocation multiply(double m) {
		return null;
	}

	@Override
	public Vector3D toVector() {
		return null;
	}

	@Override
	public MCLocation subtract(MCLocation vec) {
		return null;
	}

	@Override
	public MCLocation subtract(Vector3D vec) {
		return null;
	}

	@Override
	public MCLocation subtract(double x, double y, double z) {
		return null;
	}

	@Override
	public void setX(double x) {

	}

	@Override
	public void setY(double y) {

	}

	@Override
	public void setZ(double z) {

	}

	@Override
	public void setPitch(float p) {

	}

	@Override
	public void setYaw(float y) {

	}

	@Override
	public void breakBlock() {

	}

	@Override
	public Vector3D getDirection() {
		return null;
	}

	@Override
	public MCLocation clone() {
		return new SpongeMCLocation(loc);
	}

	@Override
	public Location getHandle() {
		return loc;
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
	public int hashCode() {
		return getHandle().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return getHandle().equals(obj);
	}

	@Override
	public String toString() {
		return getHandle().toString();
	}
}
