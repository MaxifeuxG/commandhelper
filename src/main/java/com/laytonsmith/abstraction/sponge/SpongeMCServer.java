package com.laytonsmith.abstraction.sponge;

import com.google.common.base.Optional;
import com.laytonsmith.abstraction.MCCommandException;
import com.laytonsmith.abstraction.MCCommandSender;
import com.laytonsmith.abstraction.MCConsoleCommandSender;
import com.laytonsmith.abstraction.MCInventory;
import com.laytonsmith.abstraction.MCInventoryHolder;
import com.laytonsmith.abstraction.MCItemFactory;
import com.laytonsmith.abstraction.MCItemStack;
import com.laytonsmith.abstraction.MCOfflinePlayer;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.abstraction.MCRecipe;
import com.laytonsmith.abstraction.MCScoreboard;
import com.laytonsmith.abstraction.MCServer;
import com.laytonsmith.abstraction.MCWorld;
import com.laytonsmith.abstraction.enums.MCInventoryType;
import com.laytonsmith.abstraction.pluginmessages.MCMessenger;
import com.laytonsmith.abstraction.sponge.entities.SpongeMCPlayer;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.sink.MessageSinks;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.world.World;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * SpongeMCServer, 4/8/2015 4:45 PM
 *
 * @author jb_aero
 */
public class SpongeMCServer extends SpongeMCGame implements MCServer {

	private final SpongeMCConsole console;
	private final HashMap<String, SpongeMCPlayer> players;

	public SpongeMCServer() {
		console = new SpongeMCConsole(_Server().getConsole());
		players = new HashMap<>();
	}

	public SpongeMCPlayer cachePlayer(Player player) {
		SpongeMCPlayer ret = new SpongeMCPlayer(player);
		players.put(player.getName(), ret);
		return ret;
	}

	public void deCachePlayer(Player player) {
		players.remove(player.getName());
	}

	public SpongeMCPlayer getPlayer(Player player) {
		SpongeMCPlayer ret = players.get(player.getName());
		return ret == null ? cachePlayer(player) : ret;
	}

	@Override
	public Object getHandle() {
		return _Game().getServer();
	}

	public Server _Server() {
		return _Game().getServer();
	}

	@Override
	public String getName() {
		return _Game().getPlatform().getName();
	}

	@Override
	public Collection<MCPlayer> getOnlinePlayers() {
		ArrayList<MCPlayer> ret = new ArrayList<>();
		for (Player p : _Server().getOnlinePlayers()) {
			ret.add(getPlayer(p));
		}
		return ret;
	}

	@Override
	public boolean dispatchCommand(MCCommandSender cs, String string) throws MCCommandException {
		return game.getCommandDispatcher().process((CommandSource) cs.getHandle(), string).isPresent();
	}

	@Override
	public MCPlayer getPlayer(String name) {
		Optional<Player> p = _Server().getPlayer(name);
		if (p.isPresent()) {
			return getPlayer(p.get());
		}
		return null;
	}

	@Override
	public MCPlayer getPlayer(UUID uuid) {
		Optional<Player> p = _Server().getPlayer(uuid);
		if (p.isPresent()) {
			return getPlayer(p.get());
		}
		return null;
	}

	@Override
	public MCWorld getWorld(String name) {
		Optional<World> w = _Server().getWorld(name);
		if (w.isPresent()) {
			return new SpongeMCWorld(w.get());
		}
		return null;
	}

	@Override
	public List<MCWorld> getWorlds() {
		ArrayList<MCWorld> worlds = new ArrayList<>();
		for (World w : _Server().getWorlds()) {
			worlds.add(new SpongeMCWorld(w));
		}
		return worlds;
	}

	@Override
	public void broadcastMessage(String message) {
		_Server().getBroadcastSink().sendMessage(Texts.of(message));
	}

	@Override
	public void broadcastMessage(String message, String permission) {
		MessageSinks.toPermission(permission).sendMessage(Texts.of(message));
	}

	@Override
	public MCConsoleCommandSender getConsole() {
		return console;
	}

	@Override
	public MCItemFactory getItemFactory() {
		return null;
	}

	@Override
	public MCInventory createInventory(MCInventoryHolder owner, MCInventoryType type) {
		return null;
	}

	@Override
	public MCInventory createInventory(MCInventoryHolder owner, int size, String title) {
		return null;
	}

	@Override
	public MCInventory createInventory(MCInventoryHolder owner, int size) {
		return null;
	}

	@Override
	public MCOfflinePlayer getOfflinePlayer(String player) {
		return null;
	}

	@Override
	public MCOfflinePlayer getOfflinePlayer(UUID uuid) {
		return null;
	}

	@Override
	public MCOfflinePlayer[] getOfflinePlayers() {
		return new MCOfflinePlayer[0];
	}

	@Override
	public String getServerName() {
		return getName();
	}

	@Override
	public int getPort() {
		return _Server().getBoundAddress().or(InetSocketAddress.createUnresolved("0.0.0.0", 25565)).getPort();
	}

	@Override
	public String getIp() {
		return _Server().getBoundAddress().or(InetSocketAddress.createUnresolved("0.0.0.0", 25565)).getHostString();
	}

	@Override
	public Boolean getAllowFlight() {
		return null;
	}

	@Override
	public Boolean getAllowNether() {
		return null;
	}

	@Override
	public Boolean getAllowEnd() {
		return null;
	}

	@Override
	public Boolean getOnlineMode() {
		return null;
	}

	@Override
	public int getViewDistance() {
		return 0;
	}

	@Override
	public String getWorldContainer() {
		return null;
	}

	@Override
	public int getMaxPlayers() {
		return _Server().getMaxPlayers();
	}

	@Override
	public List<MCOfflinePlayer> getBannedPlayers() {
		return null;
	}

	@Override
	public List<MCOfflinePlayer> getWhitelistedPlayers() {
		return null;
	}

	@Override
	public List<MCOfflinePlayer> getOperators() {
		return null;
	}

	@Override
	public void banIP(String address) {

	}

	@Override
	public Set<String> getIPBans() {
		return null;
	}

	@Override
	public void unbanIP(String address) {

	}

	@Override
	public MCScoreboard getMainScoreboard() {
		return null;
	}

	@Override
	public MCScoreboard getNewScoreboard() {
		return null;
	}

	@Override
	public void runasConsole(String cmd) {
		game.getCommandDispatcher().process(game.getServer().getConsole(), cmd);
	}

	@Override
	public MCMessenger getMessenger() {
		return new SpongeMCMessenger(game.getServer());
	}

	@Override
	public boolean unloadWorld(MCWorld world, boolean save) {
		return false;
	}

	@Override
	public boolean addRecipe(MCRecipe recipe) {
		return false;
	}

	@Override
	public List<MCRecipe> getRecipesFor(MCItemStack result) {
		return null;
	}

	@Override
	public List<MCRecipe> allRecipes() {
		return null;
	}

	@Override
	public void clearRecipes() {

	}

	@Override
	public void resetRecipes() {

	}

	@Override
	public void savePlayers() {

	}

	@Override
	public void shutdown() {
		game.getServer().shutdown();
	}

	@Override
	public String dispatchAndCaptureCommand(MCCommandSender commandSender, String cmd) {
		CommandSource src = ((CommandSource) commandSender.getHandle());
		// todo proxy this to capture messages
		return null;
	}
}
