package com.laytonsmith.abstraction.enums.sponge;

import com.laytonsmith.abstraction.MCEntity;
import com.laytonsmith.abstraction.enums.MCEntityType;
import com.laytonsmith.core.CHLog;
import com.laytonsmith.core.constructs.Target;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jb_aero on 4/6/2015.
 */
public class SpongeMCEntityType extends MCEntityType {

	private EntityType concrete;

	public SpongeMCEntityType(EntityType concreteType, MCEntityType.MCVanillaEntityType abstractedType) {
		super(abstractedType);
		concrete = concreteType;
	}

	// This way we don't take up extra memory on non-bukkit implementations
	public static void build(GameRegistry registry) {
		vanilla = new HashMap<>();
		mappings = new HashMap<>();
		NULL = new SpongeMCEntityType(EntityTypes.UNKNOWN, MCEntityType.MCVanillaEntityType.UNKNOWN);
		ArrayList<EntityType> counted = new ArrayList<>();
		for (MCEntityType.MCVanillaEntityType v : MCEntityType.MCVanillaEntityType.values()) {
			if (v.existsInCurrent()) {
				EntityType type = getSpongeType(registry, v);
				if (type == null) {
					CHLog.GetLogger().e(CHLog.Tags.RUNTIME, "Could not find a matching entity type for " + v.name()
							+ ". This is an error, please report this to the bug tracker.", Target.UNKNOWN);
					continue;
				}
				SpongeMCEntityType wrapper = new SpongeMCEntityType(type, v);
				wrapper.setWrapperClass();
				vanilla.put(v, wrapper);
				mappings.put(v.name(), wrapper);
				counted.add(type);
			}
		}
		for (EntityType b : registry.getAllOf(CatalogTypes.ENTITY_TYPE)) {
			if (!counted.contains(b)) {
				mappings.put(b.getId(), new SpongeMCEntityType(b, MCEntityType.MCVanillaEntityType.UNKNOWN));
			}
		}
	}

	@Override
	public String name() {
		return abstracted == MCEntityType.MCVanillaEntityType.UNKNOWN ? concrete.getId() : abstracted.name();
	}

	@Override
	public String concreteName() {
		return concrete.getId();
	}

	@Override
	public EntityType getConcrete() {
		return concrete;
	}

	@Override
	public boolean isSpawnable() {
		return (abstracted == MCEntityType.MCVanillaEntityType.UNKNOWN) ? (concrete
				!= EntityTypes.UNKNOWN) : abstracted.isSpawnable();
	}

	public static MCEntityType valueOfConcrete(EntityType test) {
		return valueOfConcrete(test.getId());
	}

	public static MCEntityType valueOfConcrete(String test) {
		for (MCEntityType t : mappings.values()) {
			if (((SpongeMCEntityType) t).concreteName().equals(test)) {
				return t;
			}
		}
		return NULL;
	}

	// Add exceptions here
	public static EntityType getSpongeType(GameRegistry reg, MCEntityType.MCVanillaEntityType v) {
		switch (v) {
			case ENDER_EYE:
				return EntityTypes.EYE_OF_ENDER;
			case MINECART:
				return EntityTypes.RIDEABLE_MINECART;
			case MINECART_CHEST:
				return EntityTypes.CHESTED_MINECART;
			case MINECART_COMMAND:
				return EntityTypes.COMMANDBLOCK_MINECART;
			case MINECART_FURNACE:
				return EntityTypes.FURNACE_MINECART;
			case MINECART_HOPPER:
				return EntityTypes.HOPPER_MINECART;
			case MINECART_MOB_SPAWNER:
				return EntityTypes.MOB_SPAWNER_MINECART;
			case MINECART_TNT:
				return EntityTypes.TNT_MINECART;
		}
		return reg.getType(CatalogTypes.ENTITY_TYPE, v.name()).orNull();
	}

	// This is here because it shouldn't be getting changed from API
	public void setWrapperClass(Class<? extends MCEntity> clazz) {
		wrapperClass = clazz;
	}

	// run once on setup
	private void setWrapperClass() {
		switch (abstracted) {
			case UNKNOWN:
				//wrapperClass = BukkitMCEntity.class;
				break;
			case DROPPED_ITEM:
				//wrapperClass = BukkitMCItem.class;
				break;
			case PRIMED_TNT:
				//wrapperClass = BukkitMCTNT.class;
				break;
			case LIGHTNING:
				//wrapperClass = BukkitMCLightningStrike.class;
				break;
			case FALLING_BLOCK:
				//wrapperClass = BukkitMCFallingBlock.class;
				break;
			case SPLASH_POTION:
				//wrapperClass = BukkitMCThrownPotion.class;
				break;
			default:
				String[] split = abstracted.name().toLowerCase().split("_");
				if (split.length == 0 || "".equals(split[0])) {
					break;
				}
				String name = "com.laytonsmith.abstraction.sponge.entities.SpongeMC";
				if ("minecart".equals(split[0])) {
					if (split.length == 1 || !"command".equals(split[1])) {
						//wrapperClass = BukkitMCMinecart.class;
						break;
					} else {
						//wrapperClass = BukkitMCCommandMinecart.class;
						break;
					}
				}
				if (split[0].startsWith("fish")) { // Bukkit enum matches neither the old class or the new
					//wrapperClass = BukkitMCFishHook.class;
					break;
				}
				for (String s : split) {
					name = name.concat(Character.toUpperCase(s.charAt(0)) + s.substring(1));
				}
				try {
					wrapperClass = (Class<? extends MCEntity>) Class.forName(name);
				} catch (ClassNotFoundException e) {
					String url = "https://github.com/sk89q/CommandHelper/tree/master/src/main/java/"
							+ "com/laytonsmith/abstraction/sponge/entities";
					CHLog.GetLogger().d(CHLog.Tags.RUNTIME, "While trying to find the correct entity class for "
							+ abstracted.name() + "(attempted " + name + "), we were unable to find a wrapper class."
							+ " This is not necessarily an error, we just don't have any special handling for"
							+ " this entity yet, and will treat it generically. If there is a matching file at"
							+ url + ", please alert the developers of this notice.", Target.UNKNOWN);
				}
		}
	}
}
