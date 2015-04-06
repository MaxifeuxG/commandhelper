package com.laytonsmith.abstraction.sponge;

import com.laytonsmith.abstraction.MCChunk;
import com.laytonsmith.abstraction.MCEntity;
import com.laytonsmith.abstraction.MCWorld;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.world.Chunk;

import java.util.ArrayList;

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
	public int getX() {
		return getHandle().getPosition().getX();
	}

	@Override
	public int getZ() {
		return getHandle().getPosition().getZ();
	}

	@Override
	public MCWorld getWorld() {
		return new SpongeMCWorld(getHandle().getWorld());
	}

	@Override
	public MCEntity[] getEntities() {
		ArrayList<Entity> collection = new ArrayList<>(getHandle().getEntities());
		ArrayList<MCEntity> ret = new ArrayList<>();
		for (Entity ent : collection) {
			ret.add(SpongeConvertor.SpongeGetCorrectEntity(ent));
		}
		return (MCEntity[]) ret.toArray();
	}

	@Override
	public Chunk getHandle() {
		return chunk;
	}
}
