package com.laytonsmith.abstraction.sponge;

import com.laytonsmith.abstraction.MCChunk;
import com.laytonsmith.abstraction.MCEntity;
import com.laytonsmith.abstraction.MCItem;
import com.laytonsmith.abstraction.MCItemStack;
import com.laytonsmith.abstraction.MCLightningStrike;
import com.laytonsmith.abstraction.MCLivingEntity;
import com.laytonsmith.abstraction.MCLocation;
import com.laytonsmith.abstraction.MCMetadataValue;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.abstraction.MCPlugin;
import com.laytonsmith.abstraction.MCWorld;
import com.laytonsmith.abstraction.blocks.MCBlock;
import com.laytonsmith.abstraction.blocks.MCFallingBlock;
import com.laytonsmith.abstraction.enums.MCBiomeType;
import com.laytonsmith.abstraction.enums.MCDifficulty;
import com.laytonsmith.abstraction.enums.MCEffect;
import com.laytonsmith.abstraction.enums.MCEntityType;
import com.laytonsmith.abstraction.enums.MCGameRule;
import com.laytonsmith.abstraction.enums.MCMobs;
import com.laytonsmith.abstraction.enums.MCSound;
import com.laytonsmith.abstraction.enums.MCTreeType;
import com.laytonsmith.abstraction.enums.MCWorldEnvironment;
import com.laytonsmith.abstraction.enums.MCWorldType;
import com.laytonsmith.abstraction.enums.sponge.SpongeMCEntityType;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.Target;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * SpongeWorld, 4/7/2015 7:09 PM
 *
 * @author jb_aero
 */
public class SpongeMCWorld implements MCWorld {

	private World world;

	public SpongeMCWorld(World param) {
		world = param;
	}

	@Override
	public List<MCPlayer> getPlayers() {
		ArrayList<MCPlayer> list = new ArrayList<>();
		for (MCPlayer pl : Static.getServer().getOnlinePlayers()) {
			if (pl.getWorld().getHandle() == getHandle()) {
				list.add(pl);
			}
		}
		return list;
	}

	@Override
	public List<MCEntity> getEntities() {
		List<MCEntity> ret = new ArrayList<>();
		for (Entity e : world.getEntities()) {
			ret.add(SpongeConvertor.SpongeGetCorrectEntity(e));
		}
		return ret;
	}

	@Override
	public List<MCLivingEntity> getLivingEntities() {
		List<MCLivingEntity> ret = new ArrayList<>();
		for (Entity e : world.getEntities()) {
			MCEntity candidate = SpongeConvertor.SpongeGetCorrectEntity(e);
			if (candidate instanceof MCLivingEntity) {
				ret.add((MCLivingEntity) candidate);
			}
		}
		return ret;
	}

	@Override
	public String getName() {
		return world.getName();
	}

	@Override
	public long getSeed() {
		return getHandle().getCreationSettings().getSeed();
	}

	@Override
	public MCWorldEnvironment getEnvironment() {
		// TODO fix this
		return MCWorldEnvironment.NORMAL;
	}

	@Override
	public String getGeneratorName() {
		try {
			return getHandle().getWorldGenerator().toString();
		} catch (NullPointerException npe) {
			return "default";
		}
	}

	@Override
	public MCWorldType getWorldType() {
		// TODO fix this
		return MCWorldType.NORMAL;
	}

	@Override
	public MCDifficulty getDifficulty() {
		// TODO someday this may break
		return MCDifficulty.valueOf(world.getDifficulty().toString());
	}

	@Override
	public void setDifficulty(MCDifficulty difficulty) {
		// TODO fix this
	}

	@Override
	public boolean getPVP() {
		// TODO fix this
		return true;
	}

	@Override
	public void setPVP(boolean pvp) {
		// TODO fix this
	}

	@Override
	public boolean getGameRuleValue(MCGameRule gameRule) {
		return false;
	}

	@Override
	public void setGameRuleValue(MCGameRule gameRule, boolean value) {

	}

	@Override
	public MCBlock getBlockAt(int x, int y, int z) {
		return null;
	}

	@Override
	public MCChunk getChunkAt(int x, int z) {
		return null;
	}

	@Override
	public MCChunk getChunkAt(MCBlock b) {
		return null;
	}

	@Override
	public MCChunk getChunkAt(MCLocation l) {
		return null;
	}

	@Override
	public MCChunk[] getLoadedChunks() {
		return new MCChunk[0];
	}

	@Override
	public boolean regenerateChunk(int x, int y) {
		return false;
	}

	@Override
	public MCEntity spawn(MCLocation l, Class mobType) {
		return null;
	}

	@Override
	public MCEntity spawn(MCLocation l, MCEntityType entType) {
		((SpongeMCEntityType) entType).getConcrete();
		return null;
	}

	@Override
	public MCEntity spawn(MCLocation l, MCEntityType.MCVanillaEntityType entityType) {
		return null;
	}

	@Override
	public boolean generateTree(MCLocation l, MCTreeType treeType) {
		return false;
	}

	@Override
	public void playEffect(MCLocation l, MCEffect mCEffect, int e, int data) {

	}

	@Override
	public void playSound(MCLocation l, MCSound sound, float volume, float pitch) {

	}

	@Override
	public void playSound(MCLocation l, String sound, float volume, float pitch) {

	}

	@Override
	public MCItem dropItemNaturally(MCLocation l, MCItemStack is) {
		return null;
	}

	@Override
	public MCItem dropItem(MCLocation l, MCItemStack is) {
		return null;
	}

	@Override
	public MCLightningStrike strikeLightning(MCLocation GetLocation) {
		return null;
	}

	@Override
	public MCLightningStrike strikeLightningEffect(MCLocation GetLocation) {
		return null;
	}

	@Override
	public void setStorm(boolean b) {

	}

	@Override
	public void setThundering(boolean b) {

	}

	@Override
	public void setWeatherDuration(int time) {

	}

	@Override
	public void setThunderDuration(int time) {

	}

	@Override
	public boolean isStorming() {
		return false;
	}

	@Override
	public boolean isThundering() {
		return false;
	}

	@Override
	public MCLocation getSpawnLocation() {
		return null;
	}

	@Override
	public void setSpawnLocation(int x, int y, int z) {

	}

	@Override
	public void refreshChunk(int x, int z) {

	}

	@Override
	public void loadChunk(int x, int z) {

	}

	@Override
	public void unloadChunk(int x, int z) {

	}

	@Override
	public void setTime(long time) {

	}

	@Override
	public long getTime() {
		return 0;
	}

	@Override
	public CArray spawnMob(MCMobs name, String subClass, int qty, MCLocation location, Target t) {
		return null;
	}

	@Override
	public MCFallingBlock spawnFallingBlock(MCLocation loc, int type, byte data) {
		return null;
	}

	@Override
	public MCBiomeType getBiome(int x, int z) {
		return null;
	}

	@Override
	public void setBiome(int x, int z, MCBiomeType type) {

	}

	@Override
	public MCBlock getHighestBlockAt(int x, int z) {
		return null;
	}

	@Override
	public void explosion(double x, double y, double z, float size, boolean safe) {

	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public void save() {

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
	public World getHandle() {
		return world;
	}
}
