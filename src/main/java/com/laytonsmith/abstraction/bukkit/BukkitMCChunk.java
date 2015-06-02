package com.laytonsmith.abstraction.bukkit;

import com.laytonsmith.abstraction.MCChunk;
import com.laytonsmith.abstraction.MCEntity;
import com.laytonsmith.abstraction.MCWorld;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author import
 */
public class BukkitMCChunk implements MCChunk {
	Chunk c;

	public BukkitMCChunk(Chunk c) {
		this.c = c;
	}

	@Override
	public boolean isLoaded() {
		return getHandle().isLoaded();
	}

	@Override
	public int getX() {
		return c.getX();
	}

	@Override
	public int getZ() {
		return c.getZ();
	}

	@Override
	public List<MCEntity> getEntities() {
		List<MCEntity> ret = new ArrayList<>();
		for (Entity entity : getHandle().getEntities()) {
			ret.add(BukkitConvertor.BukkitGetCorrectEntity(entity));
		}
		return ret;
	}

	@Override
	public MCWorld getWorld() {
		return new BukkitMCWorld(c.getWorld());
	}
	
	@Override
	public Chunk getHandle() {
		return c;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof MCChunk && this.c.equals(((BukkitMCChunk) o).c);
	}

	@Override
	public int hashCode() {
		return c.hashCode();
}

	@Override
	public String toString() {
		return c.toString();
	}
}
