package com.laytonsmith.abstraction.sponge.entities;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.laytonsmith.abstraction.MCCommandSender;
import com.laytonsmith.abstraction.MCItemStack;
import com.laytonsmith.abstraction.MCLocation;
import com.laytonsmith.abstraction.MCNote;
import com.laytonsmith.abstraction.MCOfflinePlayer;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.abstraction.MCPlayerInventory;
import com.laytonsmith.abstraction.MCScoreboard;
import com.laytonsmith.abstraction.enums.MCInstrument;
import com.laytonsmith.abstraction.enums.MCSound;
import com.laytonsmith.abstraction.enums.MCWeather;
import com.laytonsmith.commandhelper.CommandHelperSponge;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.exceptions.MarshalException;
import com.laytonsmith.core.functions.FileHandling;
import org.spongepowered.api.data.manipulator.DisplayNameData;
import org.spongepowered.api.data.manipulator.entity.ExperienceHolderData;
import org.spongepowered.api.data.manipulator.entity.FlyingData;
import org.spongepowered.api.data.manipulator.entity.FoodData;
import org.spongepowered.api.data.manipulator.entity.SneakingData;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.resourcepack.ResourcePacks;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.Tristate;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

/**
 * SpongeMCPlayer, 6/5/2015 11:03 PM
 *
 * @author jb_aero
 */
public class SpongeMCPlayer extends SpongeMCHumanEntity implements MCPlayer, MCCommandSender, MCOfflinePlayer {

	final Player player;

	public SpongeMCPlayer(Entity p) {
		super(p);
		player = (Player) p;
	}

	public static SpongeMCPlayer Get(Player p) {
		return CommandHelperSponge.self.myGame.getAbstractServer().getPlayer(p);
	}

	@Override
	public Player getHandle() {
		return player;
	}

	@Override
	public boolean canSee(MCPlayer p) {
		return false;
	}

	@Override
	public void chat(String chat) {
		if (chat.startsWith("/")) {
			Static.getServer().dispatchCommand(this, chat.substring(1));
		} else {
			getHandle().getMessageSink().sendMessage(Texts.legacy().fromUnchecked(chat)); // todo: either throw event or get this added to sponge
			// (SpongePowered/SpongeAPI#733)
		}
	}

	@Override
	public InetSocketAddress getAddress() {
		try {
			return getHandle().getConnection().getAddress();
		} catch (ClassCastException e) {
			return new InetSocketAddress("127.0.0.1", 0);
		}
	}

	@Override
	public boolean getAllowFlight() {
		return false;
	}

	@Override
	public MCLocation getCompassTarget() {
		return null;
	}

	@Override
	public String getDisplayName() {
		return getHandle().getOrCreate(DisplayNameData.class).get().getDisplayName().toString();
	}

	@Override
	public float getExp() {
		return 0;
	}

	@Override
	public float getFlySpeed() {
		return 0;
	}

	@Override
	public void setFlySpeed(float speed) {

	}

	@Override
	public int getFoodLevel() {
		return (int) getHandle().getOrCreate(FoodData.class).get().getFoodLevel();
	}

	@Override
	public MCItemStack getItemAt(Integer slot) {
		return null;
	}

	@Override
	public int getLevel() {
		return getHandle().getData(ExperienceHolderData.class).get().getLevel();
	}

	@Override
	public String getPlayerListName() {
		return null;
	}

	@Override
	public long getPlayerTime() {
		return 0;
	}

	@Override
	public MCWeather getPlayerWeather() {
		return null;
	}

	@Override
	public int getRemainingFireTicks() {
		return 0;
	}

	@Override
	public MCScoreboard getScoreboard() {
		return null;
	}

	@Override
	public int getTotalExperience() {
		return 0;
	}

	@Override
	public float getWalkSpeed() {
		return 0;
	}

	@Override
	public void setWalkSpeed(float speed) {

	}

	@Override
	public void giveExp(int xp) {

	}

	@Override
	public boolean isSneaking() {
		return getHandle().getData(SneakingData.class).isPresent();
	}

	@Override
	public boolean isSprinting() {
		return false;
	}

	@Override
	public void kickPlayer(String message) {
		getHandle().kick(Texts.legacy().fromUnchecked(message));
	}

	@Override
	public void resetPlayerTime() {

	}

	@Override
	public void resetPlayerWeather() {

	}

	@Override
	public void sendTexturePack(String url) {
		sendResourcePack(url);
	}

	@Override
	public void sendResourcePack(String url) {
		try {
			getHandle().sendResourcePack(ResourcePacks.fromUrl(new URL(url)));
		} catch (FileNotFoundException | MalformedURLException e) {
			Static.getLogger().warn("Could not send ResourcePack: ", e);
		}
	}

	@Override
	public void setAllowFlight(boolean flight) {

	}

	@Override
	public void setCompassTarget(MCLocation l) {

	}

	@Override
	public void setDisplayName(String name) {

	}

	@Override
	public void setExp(float i) {

	}

	@Override
	public void setFlying(boolean flight) {

	}

	@Override
	public void setFoodLevel(int f) {
		getHandle().getOrCreate(FoodData.class).get().setFoodLevel(f);
	}

	@Override
	public void setLevel(int xp) {
		getHandle().getOrCreate(ExperienceHolderData.class).get().setLevel(xp);
	}

	@Override
	public void setPlayerListName(String listName) {

	}

	@Override
	public void setPlayerTime(Long time, boolean relative) {

	}

	@Override
	public void setPlayerWeather(MCWeather type) {

	}

	@Override
	public void setRemainingFireTicks(int i) {

	}

	@Override
	public void setScoreboard(MCScoreboard board) {

	}

	@Override
	public void setTempOp(Boolean value) throws Exception {

	}

	@Override
	public void setTotalExperience(int total) {

	}

	@Override
	public void setVanished(boolean set, MCPlayer to) {

	}

	@Override
	public boolean isNewPlayer() {
		return false;
	}

	@Override
	public String getHost() {
		return null;
	}

	@Override
	public void sendBlockChange(MCLocation loc, int material, byte data) {

	}

	@Override
	public void sendSignTextChange(MCLocation loc, String[] lines) {

	}

	@Override
	public void playNote(MCLocation loc, MCInstrument instrument, MCNote note) {

	}

	@Override
	public void playSound(MCLocation l, MCSound sound, float volume, float pitch) {

	}

	@Override
	public void playSound(MCLocation l, String sound, float volume, float pitch) {

	}

	@Override
	public int getHunger() {
		return (int) getHandle().getOrCreate(FoodData.class).get().getFoodLevel();
	}

	@Override
	public void setHunger(int h) {
		getHandle().getOrCreate(FoodData.class).get().setFoodLevel(h);
	}

	@Override
	public float getSaturation() {
		return 0;
	}

	@Override
	public void setSaturation(float s) {

	}

	@Override
	public void setBedSpawnLocation(MCLocation l) {

	}

	@Override
	public void sendPluginMessage(String channel, byte[] message) {
		getHandle().getConnection().sendCustomPayload(CommandHelperSponge.self, channel, message);
	}

	@Override
	public void sendMessage(String string) {
		getHandle().sendMessage((new TextBuilder.Literal(string)).build());
	}

	@Override
	public boolean isOp() {
		Target t = Target.UNKNOWN;
		String read;
		Construct opList;

		try {
			read = FileHandling.read.file_get_contents("ops.json");
		} catch (Exception e) {
			Static.getLogger().info("Folder location: " + (new File(".")).getAbsolutePath());
			return false;
		}
		try {
			opList = Construct.json_decode(read, t);
		} catch (MarshalException e) {
			return false;
		}
		if (opList instanceof CArray) {
			for (Construct c : ((CArray) opList).asList()) {
				if (c instanceof CArray) {
					CArray op = (CArray) c;
					if (op.isAssociative() && op.containsKey("uuid")) {
						if (getUniqueID().toString().equals(op.get("uuid", t))) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean hasPermission(String perm) {
		return getHandle().hasPermission(perm);
	}

	@Override
	public boolean isPermissionSet(String perm) {
		return getHandle().getPermissionValue(getHandle().getActiveContexts(), perm) != Tristate.UNDEFINED;
	}

	@Override
	public List<String> getGroups() {
		final SubjectCollection groups = getGroupCollection();
		return ImmutableList.copyOf(Iterables.transform(Iterables.filter(getHandle().getParents(), new Predicate<Subject>() {
			@Override
			public boolean apply(Subject input) {
				return input.getContainingCollection().equals(groups);
			}
		}), new Function<Subject, String>() {
			@Nullable
			@Override
			public String apply(Subject input) {
				return input.getIdentifier();
			}
		}));
	}

	@Override
	public boolean inGroup(String groupName) {
		return getHandle().isChildOf(getGroupCollection().get(groupName));
	}

	private SubjectCollection getGroupCollection() {
		return CommandHelperSponge.self.theGame.getServiceManager().provideUnchecked(PermissionService.class).getGroupSubjects();
	}

	@Override
	public void setOp(boolean bln) { // TODO: Do we want to expose default/root permission API in Sponge?

	}

	@Override
	public boolean isFlying() {
		return getHandle().getData(FlyingData.class).isPresent();
	}

	@Override
	public MCPlayerInventory getInventory() {
		return null;
	}

	@Override
	public void updateInventory() {

	}

	@Override
	public long getFirstPlayed() {
		return getHandle().getJoinData().getFirstPlayed().getTime();
	}

	@Override
	public long getLastPlayed() {
		return getHandle().getJoinData().getLastPlayed().getTime();
	}

	@Override
	public MCPlayer getPlayer() {
		return this;
	}

	@Override
	public boolean hasPlayedBefore() {
		return false;
	}

	@Override
	public boolean isBanned() {
		return false;
	}

	@Override
	public boolean isOnline() {
		return getHandle().isOnline();
	}

	@Override
	public boolean isWhitelisted() {
		return false;
	}

	@Override
	public void setBanned(boolean banned) {

	}

	@Override
	public void setWhitelisted(boolean value) {

	}

	@Override
	public MCLocation getBedSpawnLocation() {
		return null;
	}

	@Override
	public UUID getUniqueID() {
		return getHandle().getUniqueId();
	}
}
