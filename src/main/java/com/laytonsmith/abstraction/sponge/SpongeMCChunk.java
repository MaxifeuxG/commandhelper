package com.laytonsmith.abstraction.sponge;

import com.laytonsmith.abstraction.MCChunk;
import com.laytonsmith.abstraction.MCEntity;
import com.laytonsmith.abstraction.MCWorld;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.world.Chunk;

import java.util.ArrayList;
import java.util.List;

/**
 * SpongeMCChunk, 5/11/2015 6:39 PM
 *
 * @author jb_aero
 */
public class SpongeMCChunk implements MCChunk {

	private Chunk chunk;

	public SpongeMCChunk(Chunk chunk) {
		this.chunk = chunk;
	}

	@Override
	public boolean isLoaded() {
		return getHandle() != null && getHandle().isLoaded();
	}

	@Override
	public int getX() {
		return isLoaded() ? getHandle().getPosition().getX() : 0;
	}

	@Override
	public int getZ() {
		return isLoaded() ? getHandle().getPosition().getZ() : 0;
	}

	@Override
	public MCWorld getWorld() {
		return new SpongeMCWorld(getHandle().getWorld());
	}

	@Override
	public List<MCEntity> getEntities() {
		ArrayList<MCEntity> ret = new ArrayList<>();
		for (Entity ent : getHandle().getEntities()) {
			ret.add(SpongeConvertor.SpongeGetCorrectEntity(ent));
		}
		return ret;
	}

	@Override
	public Chunk getHandle() {
		return chunk;
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
