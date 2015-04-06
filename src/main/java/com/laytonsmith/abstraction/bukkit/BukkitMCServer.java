package com.laytonsmith.abstraction.bukkit;

import com.laytonsmith.PureUtilities.Common.ReflectionUtils;
import com.laytonsmith.abstraction.MCCommandManager;
import com.laytonsmith.abstraction.MCCommandSender;
import com.laytonsmith.abstraction.MCConsoleCommandSender;
import com.laytonsmith.abstraction.MCHumanEntity;
import com.laytonsmith.abstraction.MCInventory;
import com.laytonsmith.abstraction.MCInventoryHolder;
import com.laytonsmith.abstraction.MCItemFactory;
import com.laytonsmith.abstraction.MCItemStack;
import com.laytonsmith.abstraction.MCOfflinePlayer;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.abstraction.MCPluginManager;
import com.laytonsmith.abstraction.MCRecipe;
import com.laytonsmith.abstraction.MCScoreboard;
import com.laytonsmith.abstraction.MCServer;
import com.laytonsmith.abstraction.MCWorld;
import com.laytonsmith.abstraction.bukkit.entities.BukkitMCHumanEntity;
import com.laytonsmith.abstraction.bukkit.entities.BukkitMCPlayer;
import com.laytonsmith.abstraction.bukkit.pluginmessages.BukkitMCMessenger;
import com.laytonsmith.abstraction.enums.MCInventoryType;
import com.laytonsmith.abstraction.pluginmessages.MCMessenger;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.Target;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.Recipe;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 *
 *
 */
public class BukkitMCServer extends BukkitMCGame implements MCServer {

	public BukkitMCServer() {
		super(Bukkit.getServer());
	}

	public Server __Server() {
		return server;
	}

	@Override
	public MCServer getServer() {
		return this;
	}

	@Override
	public String getName() {
		return server.getName();
	}

	@Override
    public Collection<MCPlayer> getOnlinePlayers() {
		Collection<Player> players = getOnlinePlayersOverwrite(server);
		if(players == null){
			return null;
		}
		Set<MCPlayer> mcpa = new HashSet<>();
		for(Player p : players){
			mcpa.add(new BukkitMCPlayer(p));
		}
        return mcpa;
    }

	/**
	 * The bukkit method getOnlinePlayers changed from returning a Player[]
	 * to a Collection<Player>. This method abstracts that out.
	 * @return
	 */
	public static Collection<Player> getOnlinePlayersOverwrite(Server s){
		Object retValue = ReflectionUtils.invokeMethod(s, "getOnlinePlayers");
        if(retValue == null){
            return null;
        }
		if(retValue instanceof Collection){
			// New version
			return (Collection<Player>) retValue;
		} else {
			// Old version, it's an array
			Set<Player> mcpa = new HashSet<>();
			Player[] pa = (Player[]) retValue;
			for(Player p : pa){
				mcpa.add(p);
			}
			return mcpa;
		}
	}

    public static MCServer Get() {
        return new BukkitMCServer();
    }

	@Override
    public boolean dispatchCommand(MCCommandSender sender, String command){
		CommandSender cs;
		if(sender instanceof BukkitMCPlayer){
			cs = (CommandSender)((MCPlayer)sender).getHandle();
		} else {
			cs = (CommandSender)((MCCommandSender)sender).getHandle();
		}
		return server.dispatchCommand(cs, command);
	}

	private class CommandSenderInterceptor implements InvocationHandler {
		private final StringBuilder buffer;
		private final CommandSender sender;

		public CommandSenderInterceptor(CommandSender sender){
			this.buffer = new StringBuilder();
			this.sender = sender;
		}

		@Override
		public Object invoke(Object o, Method method, Object[] args) throws Throwable {
			if ("sendMessage".equals(method.getName())) {
				buffer.append(args[0].toString());
				return Void.TYPE;
			} else {
				return method.invoke(sender, args);
			}
		}

		public String getBuffer(){
			return buffer.toString();
		}
	}

	@Override
	public String dispatchAndCaptureCommand(MCCommandSender commandSender, String cmd) {
		// Grab the CommandSender object from the abstraction layer
		CommandSender sender = (CommandSender)commandSender.getHandle();

		// Create the interceptor
		CommandSenderInterceptor interceptor = new CommandSenderInterceptor(sender);

		// Create a new proxy and abstraction layer wrapper around the proxy
		CommandSender newCommandSender = (CommandSender)Proxy.newProxyInstance(BukkitMCServer.class.getClassLoader(), new Class[]{CommandSender.class}, interceptor);
		BukkitMCCommandSender aCommandSender = new BukkitMCCommandSender(newCommandSender);

		MCCommandSender oldSender = Static.UninjectPlayer(commandSender);
		// Inject our new wrapped object
		Static.InjectPlayer(aCommandSender);

		// Dispatch the command now
		server.dispatchCommand(newCommandSender, cmd);

		// Clean up
		Static.UninjectPlayer(aCommandSender);
		if(oldSender != null){
			Static.InjectPlayer(oldSender);
		}

		// Return the buffered text (if any)
		return interceptor.getBuffer();
	}

	@Override
    public MCPluginManager getPluginManager() {
		if (server.getPluginManager() == null) {
			return null;
        }
		return new BukkitMCPluginManager(server.getPluginManager());
	}

	@Override
	public MCPlayer getPlayer(String name) {
		Player p = server.getPlayer(name);
		if (p == null) {
			return null;
		}
		return new BukkitMCPlayer(p);
	}

	@Override
	public MCPlayer getPlayer(UUID uuid) {
		Player p = server.getPlayer(uuid);
		if (p == null) {
			return null;
		}
		return new BukkitMCPlayer(p);
	}

	@Override
    public MCWorld getWorld(String name) {
		if (server.getWorld(name) == null) {
			return null;
        }
		return new BukkitMCWorld(server.getWorld(name));
	}

	@Override
    public List<MCWorld> getWorlds(){
		if (server.getWorlds() == null) {
			return null;
        }
        List<MCWorld> list = new ArrayList<MCWorld>();
		for (World w : server.getWorlds()) {
			list.add(new BukkitMCWorld(w));
        }
        return list;
    }

	@Override
    public void broadcastMessage(String message) {
		server.broadcastMessage(message);
	}

	@Override
	public void broadcastMessage(String message, String permission) {
		server.broadcast(message, permission);
	}

	@Override
	public MCConsoleCommandSender getConsole() {
		return new BukkitMCConsoleCommandSender(server.getConsoleSender());
	}

	@Override
	public MCItemFactory getItemFactory() {
		return new BukkitMCItemFactory(server.getItemFactory());
	}

	@Override
	public MCCommandManager getCommandMap() {
		return new BukkitMCCommandManager((SimpleCommandMap) ReflectionUtils.invokeMethod(server.getClass(), server, "getCommandMap"));
	}

	@Override
	public MCOfflinePlayer getOfflinePlayer(String player) {
		return new BukkitMCOfflinePlayer(server.getOfflinePlayer(player));
	}

	@Override
	public MCOfflinePlayer getOfflinePlayer(UUID uuid) {
		return new BukkitMCOfflinePlayer(server.getOfflinePlayer(uuid));
	}

	@Override
	public MCOfflinePlayer[] getOfflinePlayers() {
		if (server.getOfflinePlayers() == null) {
			return null;
		}
		OfflinePlayer[] offp = server.getOfflinePlayers();
		MCOfflinePlayer[] mcoff = new MCOfflinePlayer[offp.length];
		for (int i = 0; i < offp.length; i++) {
			mcoff[i] = new BukkitMCOfflinePlayer(offp[i]);
		}
		return mcoff;
	}

    /* Boring information get methods -.- */
	@Override
    public int getPort() {
		return server.getPort();
	}

	@Override
    public String getIp() {
		return server.getIp();
	}

	@Override
    public Boolean getAllowEnd() {
		return server.getAllowEnd();
	}

	@Override
    public Boolean getAllowFlight() {
		return server.getAllowFlight();
	}

	@Override
    public Boolean getAllowNether() {
		return server.getAllowNether();
	}

	@Override
    public Boolean getOnlineMode() {
		return server.getOnlineMode();
	}

	@Override
	public int getViewDistance() {
		return server.getViewDistance();
	}

	@Override
    public String getWorldContainer() {
		return server.getWorldContainer().getPath();
	}

	@Override
    public String getServerName() {
		return server.getServerName();
	}

	@Override
    public int getMaxPlayers() {
		return server.getMaxPlayers();
	}

	@Override
    public List<MCOfflinePlayer> getBannedPlayers() {
		if (server.getBannedPlayers() == null) {
			return null;
        }
        List<MCOfflinePlayer> list = new ArrayList<MCOfflinePlayer>();
		for (OfflinePlayer p : server.getBannedPlayers()) {
			list.add(getOfflinePlayer(p.getName()));
        }
        return list;
    }

	@Override
    public List<MCOfflinePlayer> getWhitelistedPlayers() {
		if (server.getBannedPlayers() == null) {
			return null;
        }
        List<MCOfflinePlayer> list = new ArrayList<MCOfflinePlayer>();
		for (OfflinePlayer p : server.getWhitelistedPlayers()) {
			list.add(getOfflinePlayer(p.getName()));
        }
        return list;
    }

	@Override
    public List<MCOfflinePlayer> getOperators() {
		if (server.getOperators() == null) {
			return null;
        }
        List<MCOfflinePlayer> list = new ArrayList<MCOfflinePlayer>();
		for (OfflinePlayer p : server.getOperators()) {
			list.add(getOfflinePlayer(p.getName()));
        }
        return list;
    }

	@Override
    public void runasConsole(String cmd) {
		CommandSender sender = (CommandSender)Static.GetCommandSender("~console", Target.UNKNOWN).getHandle();
		server.dispatchCommand(sender, cmd);
	}

	@Override
	public String toString() {
		return server.toString();
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof BukkitMCServer ? server.equals(((BukkitMCServer) obj).__Server()) : false);
	}

	@Override
	public int hashCode() {
		return server.hashCode();
	}

	@Override
	public MCInventory createInventory(MCInventoryHolder holder, MCInventoryType type) {
		InventoryHolder ih = null;

		if (holder instanceof MCPlayer) {
			ih = ((BukkitMCPlayer)holder)._Player();
		} else if (holder instanceof MCHumanEntity) {
			ih = ((BukkitMCHumanEntity)holder).asHumanEntity();
		} else if (holder.getHandle() instanceof InventoryHolder) {
			ih = (InventoryHolder)holder.getHandle();
		}

		return new BukkitMCInventory(Bukkit.createInventory(ih, InventoryType.valueOf(type.name())));
	}

	@Override
	public MCInventory createInventory(MCInventoryHolder holder, int size) {
		InventoryHolder ih = null;

		if (holder instanceof MCPlayer) {
			ih = ((BukkitMCPlayer)holder)._Player();
		} else if (holder instanceof MCHumanEntity) {
			ih = ((BukkitMCHumanEntity)holder).asHumanEntity();
		} else if (holder.getHandle() instanceof InventoryHolder) {
			ih = (InventoryHolder)holder.getHandle();
		}

		return new BukkitMCInventory(Bukkit.createInventory(ih, size));
	}

	@Override
	public MCInventory createInventory(MCInventoryHolder holder, int size, String title) {
		InventoryHolder ih = null;

		if (holder instanceof MCPlayer) {
			ih = ((BukkitMCPlayer)holder)._Player();
		} else if (holder instanceof MCHumanEntity) {
			ih = ((BukkitMCHumanEntity)holder).asHumanEntity();
		} else if (holder.getHandle() instanceof InventoryHolder) {
			ih = (InventoryHolder)holder.getHandle();
		}

		return new BukkitMCInventory(Bukkit.createInventory(ih, size, title));
	}

	@Override
	public void banIP(String address) {
		server.banIP(address);
	}

	@Override
	public Set<String> getIPBans() {
		return server.getIPBans();
	}

	@Override
	public void unbanIP(String address) {
		server.unbanIP(address);
	}

	@Override
	public MCMessenger getMessenger() {
		return new BukkitMCMessenger(server.getMessenger());
	}

	@Override
	public MCScoreboard getMainScoreboard() {
		return new BukkitMCScoreboard(server.getScoreboardManager().getMainScoreboard());
	}

	@Override
	public MCScoreboard getNewScoreboard() {
		return new BukkitMCScoreboard(server.getScoreboardManager().getNewScoreboard());
	}

	@Override
	public boolean unloadWorld(MCWorld world, boolean save) {
		return server.unloadWorld(((BukkitMCWorld) world).__World(), save);
	}

	@Override
	public void savePlayers() {
		server.savePlayers();
	}

	@Override
	public void shutdown() {
		server.shutdown();
	}

	@Override
	public boolean addRecipe(MCRecipe recipe) {
		return server.addRecipe(((BukkitMCRecipe) recipe).r);
	}

	@Override
	public List<MCRecipe> getRecipesFor(MCItemStack result) {
		List<MCRecipe> ret = new ArrayList<MCRecipe>();
		List<Recipe> recipes = server.getRecipesFor(((BukkitMCItemStack) result).__ItemStack());
		for (Recipe recipe : recipes) {
			ret.add(BukkitConvertor.BukkitGetRecipe(recipe));
		}
		return ret;
	}

	@Override
	public List<MCRecipe> allRecipes() {
		List<MCRecipe> ret = new ArrayList<MCRecipe>();
		for (Iterator recipes = server.recipeIterator(); recipes.hasNext(); ) {
			Recipe recipe = (Recipe) recipes.next();
			ret.add(BukkitConvertor.BukkitGetRecipe(recipe));
		}
		return ret;
	}

//	public Iterator<MCRecipe> recipe iterator() {
//		Iterator<MCRecipe> ret = //create iterator;
//	}

	@Override
	public void clearRecipes() {
		server.clearRecipes();
	}

	@Override
	public void resetRecipes() {
		server.resetRecipes();
	}
}
