
package com.laytonsmith.abstraction.enums;

import com.laytonsmith.PureUtilities.ClassLoading.DynamicEnum;
import com.laytonsmith.abstraction.MCEntity;
import com.laytonsmith.annotations.MDynamicEnum;
import com.laytonsmith.annotations.MEnum;
import com.laytonsmith.core.Static;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * 
 */
@MDynamicEnum("EntityType")
public abstract class MCEntityType extends DynamicEnum {

	@MEnum("VanillaEntityType")
	public enum MCVanillaEntityType {
		/**
		 * Spawn with world.dropItem()
		 */
		DROPPED_ITEM(true, MCVersion.MC1_0),
		EXPERIENCE_ORB(true, MCVersion.MC1_0),
		PAINTING(false, MCVersion.MC1_0),
		ARROW(true, MCVersion.MC1_0),
		SNOWBALL(true, MCVersion.MC1_0),
		FIREBALL(true, MCVersion.MC1_0),
		SMALL_FIREBALL(true, MCVersion.MC1_0),
		ITEM_FRAME(false, MCVersion.MC1_4_5),
		WITHER_SKULL(true, MCVersion.MC1_4),
		WITHER(true, MCVersion.MC1_4),
		BAT(true, MCVersion.MC1_4),
		WITCH(true, MCVersion.MC1_4_5),
		ENDER_PEARL(false, MCVersion.MC1_0),
		ENDER_SIGNAL(false, MCVersion.MC1_0),
		THROWN_EXP_BOTTLE(true, MCVersion.MC1_0),
		PRIMED_TNT(true, MCVersion.MC1_0),
		/**
		 * Spawn with world.spawnFallingBlock()
		 */
		FALLING_BLOCK(true, MCVersion.MC1_0),
		MINECART(true, MCVersion.MC1_0),
		BOAT(true, MCVersion.MC1_0),
		CREEPER(true, MCVersion.MC1_0),
		SKELETON(true, MCVersion.MC1_0),
		SPIDER(true, MCVersion.MC1_0),
		GIANT(true, MCVersion.MC1_0),
		ZOMBIE(true, MCVersion.MC1_0),
		SLIME(true, MCVersion.MC1_0),
		GHAST(true, MCVersion.MC1_0),
		PIG_ZOMBIE(true, MCVersion.MC1_0),
		ENDERMAN(true, MCVersion.MC1_0),
		CAVE_SPIDER(true, MCVersion.MC1_0),
		SILVERFISH(true, MCVersion.MC1_0),
		BLAZE(true, MCVersion.MC1_0),
		MAGMA_CUBE(true, MCVersion.MC1_0),
		ENDER_DRAGON(true, MCVersion.MC1_0),
		PIG(true, MCVersion.MC1_0),
		SHEEP(true, MCVersion.MC1_0),
		COW(true, MCVersion.MC1_0),
		CHICKEN(true, MCVersion.MC1_0),
		SQUID(true, MCVersion.MC1_0),
		WOLF(true, MCVersion.MC1_0),
		MUSHROOM_COW(true, MCVersion.MC1_0),
		SNOWMAN(true, MCVersion.MC1_0),
		OCELOT(true, MCVersion.MC1_2),
		IRON_GOLEM(true, MCVersion.MC1_2),
		VILLAGER(true, MCVersion.MC1_0),
		HORSE(true, MCVersion.MC1_6),
		LEASH_HITCH(false, MCVersion.MC1_6),
		ENDER_CRYSTAL(true, MCVersion.MC1_0),
		// These don't have an entity ID in nms.EntityTypes.
		SPLASH_POTION(true, MCVersion.MC1_0),
		EGG(true, MCVersion.MC1_0),
		FISHING_HOOK(false, MCVersion.MC1_0),
		/**
		 * Spawn with world.strikeLightning()
		 */
		LIGHTNING(true, MCVersion.MC1_0),
		WEATHER(true, MCVersion.MC1_0),
		PLAYER(false, MCVersion.MC1_0),
		COMPLEX_PART(false, MCVersion.MC1_0),
		FIREWORK(true, MCVersion.MC1_4_7),
		MINECART_CHEST(true, MCVersion.MC1_0),
		MINECART_FURNACE(true, MCVersion.MC1_0),
		MINECART_TNT(true, MCVersion.MC1_5),
		MINECART_HOPPER(true, MCVersion.MC1_5),
		MINECART_MOB_SPAWNER(true, MCVersion.MC1_5),
		MINECART_COMMAND(false, MCVersion.MC1_7),
		ARMOR_STAND(true, MCVersion.MC1_8),
		ENDERMITE(true, MCVersion.MC1_8),
		GUARDIAN(true, MCVersion.MC1_8),
		RABBIT(true, MCVersion.MC1_8),
		/**
		 * An unknown entity without an Entity Class
		 */
		UNKNOWN(false, MCVersion.MC1_0);

		private final boolean apiCanSpawn;
		private final MCVersion version;

		/**
		 * @param spawnable true if the entity is spawnable
		 * @param added the version this entity was added
		 */
		MCVanillaEntityType(boolean spawnable, MCVersion added) {
			this.apiCanSpawn = spawnable;
			this.version = added;
		}

		// This is here only for site-based documentation of some functions
		public boolean isSpawnable() {
			return this.apiCanSpawn;
		}

		public boolean existsInCurrent() {
			return Static.getServer().getMinecraftVersion().ordinal() >= version.ordinal();
		}
	}

	// To be filled by the implementer
	protected static Map<String, MCEntityType> mappings;
	protected static Map<MCVanillaEntityType, MCEntityType> vanilla;
	protected static Map<MCVanillaEntityType, Class<? extends MCEntity>> classList;

	public static MCEntityType NULL = null;

	public MCEntityType(MCVanillaEntityType abstractedType) {
		abstracted = abstractedType;
	}

	// Instance variable;
	protected MCVanillaEntityType abstracted;

	/**
	 * @return always returns the concrete name
	 */
	public abstract String concreteName();

	public abstract Object getConcrete();

	/**
	 * Utility method for spawn_entity
	 * @return whether the implemented api can spawn this entity
	 */
	public abstract boolean isSpawnable();

	public MCVanillaEntityType getAbstracted() {
		return abstracted;
	}

	public static MCEntityType valueOf(String test) {
		MCEntityType ret = mappings.get(test);
		if (ret == null) {
			throw new IllegalArgumentException("Unknown entity type: " + test);
		}
		return ret;
	}

	public static MCEntityType valueOfVanillaType(MCVanillaEntityType type) {
		return vanilla.get(type);
	}

	/**
	 * @return Names of available entity types
	 */
	public static Set<String> types() {
		if (NULL == null) { // docs mode
			Set<String> dummy = new HashSet<>();
			for (final MCVanillaEntityType t : MCVanillaEntityType.values()) {
				dummy.add(t.name());
			}
			return dummy;
		}
		return mappings.keySet();
	}

	/**
	 * @return Our own EntityType list
	 */
	public static Collection<MCEntityType> values() {
		if (NULL == null) { // docs mode
			ArrayList<MCEntityType> dummy = new ArrayList<>();
			for (final MCVanillaEntityType t : MCVanillaEntityType.values()) {
				dummy.add(new MCEntityType(t) {
					@Override
					public String name() {
						return t.name();
					}

					@Override
					public String concreteName() {
						return t.name();
					}

					@Override
					public Object getConcrete() {
						return null;
					}

					@Override
					public boolean isSpawnable() {
						return t.isSpawnable();
					}
				});
			}
			return dummy;
		}
		return mappings.values();
	}
}