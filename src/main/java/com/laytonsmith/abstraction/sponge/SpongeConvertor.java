package com.laytonsmith.abstraction.sponge;

import com.laytonsmith.PureUtilities.Common.ReflectionUtils;
import com.laytonsmith.abstraction.AbstractConvertor;
import com.laytonsmith.abstraction.Implementation;
import com.laytonsmith.abstraction.MCColor;
import com.laytonsmith.abstraction.MCCommand;
import com.laytonsmith.abstraction.MCCommandSender;
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
import com.laytonsmith.abstraction.enums.sponge.SpongeMCEntityType;
import com.laytonsmith.abstraction.sponge.entities.SpongeMCEntity;
import com.laytonsmith.abstraction.sponge.entities.SpongeMCHumanEntity;
import com.laytonsmith.abstraction.sponge.entities.SpongeMCLivingEntity;
import com.laytonsmith.abstraction.sponge.entities.SpongeMCPlayer;
import com.laytonsmith.abstraction.sponge.events.SpongeAbstractEventMixin;
import com.laytonsmith.annotations.convert;
import com.laytonsmith.commandhelper.CommandHelperSponge;
import com.laytonsmith.core.CHLog;
import com.laytonsmith.core.LogLevel;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.functions.Exceptions;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.source.CommandBlockSource;
import org.spongepowered.api.util.command.source.ConsoleSource;

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
	public MCCommand getNewCommand(String name) {
		return new SpongeMCCommand(name);
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
		if (se == null) {
			return null;
		}

		SpongeMCEntityType type = SpongeMCEntityType.valueOfConcrete(se.getType());
		if (type.getWrapperClass() != null) {
			return ReflectionUtils.newInstance(type.getWrapperClass(), new Class[]{Entity.class}, new Object[]{se});
		}

//		if (se instanceof Hanging) {
//			type.setWrapperClass(BukkitMCHanging.class);
//			return new BukkitMCHanging(se);
//		}
//
//		if (se instanceof Minecart) {
//			// Must come before Vehicle
//			type.setWrapperClass(BukkitMCMinecart.class);
//			return new BukkitMCMinecart(se);
//		}
//
//		if (se instanceof Projectile) {
//			type.setWrapperClass(BukkitMCProjectile.class);
//			return new BukkitMCProjectile(se);
//		}
//
//		if (se instanceof Tameable) {
//			// Must come before LivingEntity
//			type.setWrapperClass(BukkitMCTameable.class);
//			return new BukkitMCTameable(se);
//		}

		if (se instanceof Human) {
			// Must come before LivingEntity
			type.setWrapperClass(SpongeMCHumanEntity.class);
			return new SpongeMCHumanEntity(se);
		}

//		if (se instanceof ComplexEntityPart) {
//			type.setWrapperClass(BukkitMCComplexEntityPart.class);
//			return new BukkitMCComplexEntityPart(se);
//		}
//
//		if (se instanceof ComplexLivingEntity) {
//			// Must come before LivingEntity
//			type.setWrapperClass(BukkitMCComplexLivingEntity.class);
//			return new BukkitMCComplexLivingEntity(se);
//		}

		if (se instanceof Living) {
			type.setWrapperClass(SpongeMCLivingEntity.class);
			return new SpongeMCLivingEntity(se);
		}

//		if (se instanceof Vehicle) {
//			type.setWrapperClass(BukkitMCVehicle.class);
//			return new BukkitMCVehicle(se);
//		}

		// Handle generically if we can't find a more specific type
		type.setWrapperClass(SpongeMCEntity.class);
		return new SpongeMCEntity(se);
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

	@Override
	public MCCommandSender GetCorrectSender(MCCommandSender unspecific) {
		return SpongeGetCorrectSender(unspecific instanceof SpongeMCCommandSender ? (CommandSource) unspecific.getHandle() : null);
	}

	public static MCCommandSender SpongeGetCorrectSender(CommandSource src) {
		if (src == null) {
			return null;
		} else if (src instanceof Player) {
			return new SpongeMCPlayer((Player) src);
		} else if (src instanceof ConsoleSource) {
			return new SpongeMCConsole((ConsoleSource) src);
		} else if (src instanceof CommandBlockSource) {
			return new SpongeMCCommandSender(src); // TODO
		} else {
			return new SpongeMCCommandSender(src);
		}
	}
}
