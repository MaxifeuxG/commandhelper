package com.laytonsmith.abstraction.sponge;

import com.laytonsmith.abstraction.AbstractConvertor;
import com.laytonsmith.abstraction.Implementation;
import com.laytonsmith.abstraction.MCColor;
import com.laytonsmith.abstraction.MCEnchantment;
import com.laytonsmith.abstraction.MCEntity;
import com.laytonsmith.abstraction.MCFireworkBuilder;
import com.laytonsmith.abstraction.MCGame;
import com.laytonsmith.abstraction.MCInventory;
import com.laytonsmith.abstraction.MCItemMeta;
import com.laytonsmith.abstraction.MCItemStack;
import com.laytonsmith.abstraction.MCLocation;
import com.laytonsmith.abstraction.MCMetadataValue;
import com.laytonsmith.abstraction.MCNote;
import com.laytonsmith.abstraction.MCPlugin;
import com.laytonsmith.abstraction.MCPluginMeta;
import com.laytonsmith.abstraction.MCRecipe;
import com.laytonsmith.abstraction.MCServer;
import com.laytonsmith.abstraction.MCWorld;
import com.laytonsmith.abstraction.blocks.MCMaterial;
import com.laytonsmith.abstraction.enums.MCRecipeType;
import com.laytonsmith.abstraction.enums.MCTone;
import com.laytonsmith.abstraction.sponge.entities.SpongeMCEntity;
import com.laytonsmith.abstraction.sponge.events.SpongeAbstractEventMixin;
import com.laytonsmith.annotations.convert;
import com.laytonsmith.commandhelper.CommandHelperSponge;
import com.laytonsmith.core.CHLog;
import com.laytonsmith.core.LogLevel;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.functions.Exceptions;
import org.spongepowered.api.entity.Entity;

import java.util.List;

/**
 * SpongeConvertor, 4/7/2015 6:32 PM
 *
 * @author jb_aero
 */
@convert(type = Implementation.Type.SPONGE)
public class SpongeConvertor extends AbstractConvertor {

	public SpongeConvertor() {
		game = CommandHelperSponge.self.myGame;
	}

	@Override
	public MCLocation GetLocation(MCWorld w, double x, double y, double z, float yaw, float pitch) {
		return null;
	}

	@Override
	public Class GetServerEventMixin() {
		return SpongeAbstractEventMixin.class;
	}

	@Override
	public MCEnchantment[] GetEnchantmentValues() {
		return new MCEnchantment[0];
	}

	@Override
	public MCEnchantment GetEnchantmentByName(String name) {
		return null;
	}

	@Override
	public MCGame GetGame() {
		return game;
	}

	@Override
	public MCServer GetServer() {
		return game.getServer();
	}

	@Override
	public MCItemStack GetItemStack(int type, int qty) {
		return null;
	}

	@Override
	public MCItemStack GetItemStack(int type, int data, int qty) {
		return null;
	}

	@Override
	public MCItemStack GetItemStack(MCMaterial type, int qty) {
		return null;
	}

	@Override
	public MCItemStack GetItemStack(MCMaterial type, int data, int qty) {
		return null;
	}

	@Override
	public MCItemStack GetItemStack(String type, int qty) {
		return null;
	}

	@Override
	public MCItemStack GetItemStack(String type, int data, int qty) {
		return null;
	}

	@Override
	public void Startup() {

	}

	@Override
	public int LookupItemId(String materialName) {
		return 0;
	}

	@Override
	public String LookupMaterialName(int id) {
		return null;
	}

	@Override
	public MCMaterial getMaterial(int id) {
		return null;
	}

	@Override
	public MCMaterial GetMaterial(String name) {
		return null;
	}

	@Override
	public MCMetadataValue GetMetadataValue(Object value, MCPlugin plugin) {
		return null;
	}

	@Override
	public MCEntity GetCorrectEntity(MCEntity e) {

		Entity be = ((SpongeMCEntity) e).getHandle();
		try {
			return SpongeGetCorrectEntity(be);
		} catch (IllegalArgumentException iae) {
			CHLog.GetLogger().Log(CHLog.Tags.RUNTIME, LogLevel.INFO, iae.getMessage(), Target.UNKNOWN);
			return e;
		}
	}

	public static MCEntity SpongeGetCorrectEntity(Entity se) {
		return null;
	}

	@Override
	public MCItemMeta GetCorrectMeta(MCItemMeta im) {
		return null;
	}

	@Override
	public List<MCEntity> GetEntitiesAt(MCLocation loc, double radius) {
		return null;
	}

	@Override
	public MCInventory GetEntityInventory(int entityID) {
		return null;
	}

	@Override
	public MCInventory GetLocationInventory(MCLocation location) {
		return null;
	}

	@Override
	public MCNote GetNote(int octave, MCTone tone, boolean sharp) {
		return null;
	}

	@Override
	public int getMaxBlockID() {
		return 0;
	}

	@Override
	public int getMaxItemID() {
		return 0;
	}

	@Override
	public int getMaxRecordID() {
		return 0;
	}

	@Override
	public MCColor GetColor(int red, int green, int blue) {
		return null;
	}

	@Override
	public MCColor GetColor(String colorName, Target t) throws Exceptions.FormatException {
		return null;
	}

	@Override
	public MCFireworkBuilder GetFireworkBuilder() {
		return null;
	}

	@Override
	public MCPluginMeta GetPluginMeta() {
		return null;
	}

	@Override
	public MCRecipe GetNewRecipe(MCRecipeType type, MCItemStack result) {
		return null;
	}

	@Override
	public MCRecipe GetRecipe(MCRecipe unspecific) {
		return null;
	}

	@Override
	public String GetPluginName() {
		return null;
	}

	@Override
	public MCPlugin GetPlugin() {
		return null;
	}
}
