package com.laytonsmith.commandhelper;

import com.laytonsmith.PureUtilities.ClassLoading.ClassDiscovery;
import com.laytonsmith.PureUtilities.ClassLoading.ClassDiscoveryCache;
import com.laytonsmith.PureUtilities.Common.FileUtil;
import com.laytonsmith.PureUtilities.Common.OSUtils;
import com.laytonsmith.PureUtilities.ExecutionQueue;
import com.laytonsmith.PureUtilities.SimpleVersion;
import com.laytonsmith.PureUtilities.TermColors;
import com.laytonsmith.abstraction.Implementation;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.abstraction.StaticLayer;
import com.laytonsmith.abstraction.enums.MCChatColor;
import com.laytonsmith.core.AbstractLogger;
import com.laytonsmith.core.AliasCore;
import com.laytonsmith.core.CHLog;
import com.laytonsmith.core.Installer;
import com.laytonsmith.core.Main;
import com.laytonsmith.core.MethodScriptCompiler;
import com.laytonsmith.core.MethodScriptComplete;
import com.laytonsmith.core.MethodScriptExecutionQueue;
import com.laytonsmith.core.MethodScriptFileLocations;
import com.laytonsmith.core.ParseTree;
import com.laytonsmith.core.Prefs;
import com.laytonsmith.core.Profiles;
import com.laytonsmith.core.Script;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.UpgradeLog;
import com.laytonsmith.core.UserManager;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.constructs.Token;
import com.laytonsmith.core.environments.CommandHelperEnvironment;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.environments.GlobalEnv;
import com.laytonsmith.core.exceptions.CancelCommandException;
import com.laytonsmith.core.exceptions.ConfigCompileException;
import com.laytonsmith.core.exceptions.ConfigCompileGroupException;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.extensions.ExtensionManager;
import com.laytonsmith.core.profiler.Profiler;
import com.laytonsmith.core.taskmanager.TaskManager;
import com.laytonsmith.persistence.DataSourceException;
import com.laytonsmith.persistence.PersistenceNetwork;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * CommandHelperMainClass, 4/6/2015
 *
 * @author jb_aero
 */
public class CommandHelperCommon {

	public static CommandHelperCommon self;
	public final AbstractLogger logger;
	public Profiler profiler;
	public static SimpleVersion version;
	public long interpreterUnlockedUntil = 0;
	public final ExecutionQueue executionQueue = new MethodScriptExecutionQueue("CommandHelperExecutionQueue", "default");
	public PersistenceNetwork persistenceNetwork;
	public static ExecutorService hostnameLookupThreadPool;
	public static ConcurrentHashMap<String, String> hostnameLookupCache;
	protected static AliasCore ac;
	protected Thread loadingThread;
	protected static int hostnameThreadPoolID = 0;

	private final Set<String> interpreterMode = Collections.synchronizedSet(new HashSet<String>());
	final Map<String, String> multilineMode = new HashMap<String, String>();

	public CommandHelperCommon(AbstractLogger logger) {
		self = this;
		this.logger = logger;
	}

	public Profiler getProfiler() {
		return profiler;
	}

	public void setProfiler(Profiler profiler) {
		this.profiler = profiler;
	}

	public ExecutionQueue getExecutionQueue() {
		return executionQueue;
	}

	public void stopExecutionQueue() {
		for (String queue : executionQueue.activeQueues()) {
			executionQueue.clear(queue);
		}
	}

	public PersistenceNetwork getPersistenceNetwork() {
		return persistenceNetwork;
	}

	public void setPersistenceNetwork(PersistenceNetwork persistenceNetwork) {
		this.persistenceNetwork = persistenceNetwork;
	}

	public static AliasCore getCore() {
		return ac;
	}

	public boolean isInInterpreterMode(String player) {
		return (interpreterMode.contains(player));
	}

	public void startInterpret(String playerName) {
		interpreterMode.add(playerName);
	}

	public void stopInterpret(String playerName) {
		interpreterMode.remove(playerName);
	}

	/**
	 * Find and run aliases for a player for a given command.
	 *
	 * @param command
	 *
	 * @return
	 */
	public boolean runAlias(String command, MCPlayer player) throws DataSourceException {
		UserManager um = UserManager.GetUserManager(player.getName());
		List<Script> scripts = um.getAllScripts(persistenceNetwork);

		return getCore().alias(command, player, scripts);
	}

	public void textLine(MCPlayer p, String line) {
		if (line.equals("-")) {
			//Exit interpreter mode
			interpreterMode.remove(p.getName());
			Static.SendMessage(p, MCChatColor.YELLOW + "Now exiting interpreter mode");
		} else if (line.equals(">>>")) {
			//Start multiline mode
			if (multilineMode.containsKey(p.getName())) {
				Static.SendMessage(p, MCChatColor.RED + "You are already in multiline mode!");
			} else {
				multilineMode.put(p.getName(), "");
				Static.SendMessage(p,
						MCChatColor.YELLOW + "You are now in multiline mode. Type <<< on a line by itself to execute.");
				Static.SendMessage(p, ":" + MCChatColor.GRAY + ">>>");
			}
		} else if (line.equals("<<<")) {
			//Execute multiline
			Static.SendMessage(p, ":" + MCChatColor.GRAY + "<<<");
			String script = multilineMode.get(p.getName());
			multilineMode.remove(p.getName());
			try {
				execute(script, p);
			} catch (ConfigCompileException e) {
				Static.SendMessage(p, MCChatColor.RED + e.getMessage() + ":" + e.getLineNum());
			} catch (ConfigCompileGroupException ex) {
				for (ConfigCompileException e : ex.getList()) {
					Static.SendMessage(p, MCChatColor.RED + e.getMessage() + ":" + e.getLineNum());
				}
			}
		} else {
			if (multilineMode.containsKey(p.getName())) {
				//Queue multiline
				multilineMode.put(p.getName(), multilineMode.get(p.getName()) + line + "\n");
				Static.SendMessage(p, ":" + MCChatColor.GRAY + line);
			} else {
				try {
					//Execute single line
					execute(line, p);
				} catch (ConfigCompileException ex) {
					Static.SendMessage(p, MCChatColor.RED + ex.getMessage());
				} catch (ConfigCompileGroupException e) {
					for (ConfigCompileException ex : e.getList()) {
						Static.SendMessage(p, MCChatColor.RED + ex.getMessage());
					}
				}
			}
		}
	}

	public void execute(String script, final MCPlayer p) throws ConfigCompileException, ConfigCompileGroupException {
		List<Token> stream = MethodScriptCompiler.lex(script, new File("Interpreter"), true);
		ParseTree tree = MethodScriptCompiler.compile(stream);
		interpreterMode.remove(p.getName());
		GlobalEnv gEnv;
		try {
			gEnv = new GlobalEnv(executionQueue, profiler, persistenceNetwork,
					CommandHelperFileLocations.getDefault().getConfigDirectory(),
					new Profiles(MethodScriptFileLocations.getDefault().getSQLProfilesFile()),
					new TaskManager());
		} catch (IOException ex) {
			CHLog.GetLogger().e(CHLog.Tags.GENERAL, ex.getMessage(), Target.UNKNOWN);
			return;
		} catch (Profiles.InvalidProfileException ex) {
			CHLog.GetLogger().e(CHLog.Tags.GENERAL, ex.getMessage(), Target.UNKNOWN);
			return;
		}
		gEnv.SetDynamicScriptingMode(true);
		CommandHelperEnvironment cEnv = new CommandHelperEnvironment();
		cEnv.SetPlayer(p);
		Environment env = Environment.createEnvironment(gEnv, cEnv);
		try {
			MethodScriptCompiler.registerAutoIncludes(env, null);
			MethodScriptCompiler.execute(tree, env, new MethodScriptComplete() {

				@Override
				public void done(String output) {
					output = output.trim();
					if (output.isEmpty()) {
						Static.SendMessage(p, ":");
					} else {
						if (output.startsWith("/")) {
							//Run the command
							Static.SendMessage(p, ":" + MCChatColor.YELLOW + output);
							p.chat(output);
						} else {
							//output the results
							Static.SendMessage(p, ":" + MCChatColor.GREEN + output);
						}
					}
					interpreterMode.add(p.getName());
				}
			}, null);
		} catch (CancelCommandException e) {
			interpreterMode.add(p.getName());
		} catch (ConfigRuntimeException e) {
			ConfigRuntimeException.HandleUncaughtException(e, env);
			Static.SendMessage(p, MCChatColor.RED + e.toString());
			interpreterMode.add(p.getName());
		} catch (Exception e) {
			Static.SendMessage(p, MCChatColor.RED + e.toString());
			Static.getLogger().error(null, e);
			interpreterMode.add(p.getName());
		}
	}

	public void firstSetup(Class apiRootClass) {

		CommandHelperFileLocations.getDefault().getCacheDirectory().mkdirs();
		CommandHelperFileLocations.getDefault().getPreferencesDirectory().mkdirs();

		UpgradeLog upgradeLog = new UpgradeLog(CommandHelperFileLocations.getDefault().getUpgradeLogFile());
		upgradeLog.addUpgradeTask(new UpgradeLog.UpgradeTask() {

			String version = null;

			@Override
			public boolean doRun() {
				try {
					version = "versionUpgrade-" + Main.loadSelfVersion();
					return !hasBreadcrumb(version);
				} catch (Exception ex) {
					logger.error(null, ex);
					return false;
				}
			}

			@Override
			public void run() {
				leaveBreadcrumb(version);
			}
		});
		upgradeLog.addUpgradeTask(new UpgradeLog.UpgradeTask() {

			File oldPreferences = new File(CommandHelperFileLocations.getDefault().getConfigDirectory(),
					"preferences.txt");

			@Override
			public boolean doRun() {
				return oldPreferences.exists()
						&& !CommandHelperFileLocations.getDefault().getPreferencesFile().exists();
			}

			@Override
			public void run() {
				try {
					Prefs.init(oldPreferences);
					Prefs.SetColors();
					logger.info(TermColors.YELLOW + "[" + Implementation.GetServerType().getBranding()
							+ "] Old preferences.txt file detected. Moving preferences.txt to preferences.ini."
							+ TermColors.reset());
					FileUtil.copy(oldPreferences, CommandHelperFileLocations.getDefault().getPreferencesFile(), true);
					oldPreferences.deleteOnExit();
				} catch (IOException ex) {
					logger.error(null, ex);
				}
			}
		});
		upgradeLog.addUpgradeTask(new UpgradeLog.UpgradeTask() {

			File cd = CommandHelperFileLocations.getDefault().getConfigDirectory();
			private final String breadcrumb = "move-preference-files-v1.0";

			@Override
			public boolean doRun() {
				return !hasBreadcrumb(breadcrumb)
						&& new File(cd, "preferences.ini").exists();
			}

			@Override
			public void run() {
				//We need to move the following files:
				//1. persistance.config to prefs/persistence.ini (note the correct spelling)
				//2. preferences.ini to prefs/preferences.ini
				//3. profiler.config to prefs/profiler.ini
				//4. sql-profiles.xml to prefs/sql-profiles.xml
				//5. We are not moving loggerPreferences.txt, instead just deleting it,
				//	because the defaults have changed. Most people aren't using this feature
				//	anyways. (The new one will write itself out upon installation.)
				//Other than the config/prefs directory, we are hardcoding all the values, so
				//we know they are correct (for old values). Any errors will be reported, but will not
				//stop the entire process.
				CommandHelperFileLocations p = CommandHelperFileLocations.getDefault();
				try {
					FileUtil.move(new File(cd, "persistance.config"), p.getPersistenceConfig());
				} catch (IOException ex) {
					logger.error(null, ex);
				}
				try {
					FileUtil.move(new File(cd, "preferences.ini"), p.getPreferencesFile());
				} catch (IOException ex) {
					logger.error(null, ex);
				}
				try {
					FileUtil.move(new File(cd, "profiler.config"), p.getProfilerConfigFile());
				} catch (IOException ex) {
					logger.error(null, ex);
				}
				try {
					FileUtil.move(new File(cd, "sql-profiles.xml"), p.getSQLProfilesFile());
				} catch (IOException ex) {
					logger.error(null, ex);
				}
				new File(cd, "logs/debug/loggerPreferences.txt").delete();
				leaveBreadcrumb(breadcrumb);
				logger.info("CommandHelper: Your preferences files have all been relocated to "
						+ p.getPreferencesDirectory());
				logger.info("CommandHelper: The loggerPreferences.txt file has been deleted and re-created, as the defaults have changed.");
			}
		});

		// Renames the sql-profiles.xml file to the new name.
		upgradeLog.addUpgradeTask(new UpgradeLog.UpgradeTask() {

			// This should never change
			private final File oldProfilesFile = new File(MethodScriptFileLocations.getDefault().getPreferencesDirectory(), "sql-profiles.xml");

			@Override
			public boolean doRun() {
				return oldProfilesFile.exists();
			}

			@Override
			public void run() {
				try {
					FileUtil.move(oldProfilesFile, MethodScriptFileLocations.getDefault().getProfilesFile());
					logger.info("CommandHelper: sql-profiles.xml has been renamed to "
							+ MethodScriptFileLocations.getDefault().getProfilesFile().getName());
				} catch (IOException ex) {
					logger.error(null, ex);
				}
			}
		});

		try {
			upgradeLog.runTasks();
		} catch (IOException ex) {
			logger.error(null, ex);
		}

		try{
			Prefs.init(CommandHelperFileLocations.getDefault().getPreferencesFile());
		} catch (IOException ex) {
			logger.error(null, ex);
		}

		Prefs.SetColors();
		CHLog.initialize(CommandHelperFileLocations.getDefault().getConfigDirectory());
		Installer.Install(CommandHelperFileLocations.getDefault().getConfigDirectory());
		if(new SimpleVersion(System.getProperty("java.version")).lt(new SimpleVersion("1.7"))){
			CHLog.GetLogger().w(CHLog.Tags.GENERAL, "You appear to be running a version of Java older than Java 7. You should have plans"
					+ " to upgrade at some point, as " + Implementation.GetServerType().getBranding() + " may require it at some point.", Target.UNKNOWN);
		}

		ClassDiscoveryCache cdc = new ClassDiscoveryCache(CommandHelperFileLocations.getDefault().getCacheDirectory());
		cdc.setLogger(logger);
		ClassDiscovery.getDefaultInstance().setClassDiscoveryCache(cdc);
		ClassDiscovery.getDefaultInstance().addDiscoveryLocation(ClassDiscovery.GetClassContainer(CommandHelperCommon.class));
		ClassDiscovery.getDefaultInstance().addDiscoveryLocation(ClassDiscovery.GetClassContainer(apiRootClass));

		logger.info("[CommandHelper] Running initial class discovery,"
				+ " this will probably take a few seconds...");

		logger.info("[CommandHelper] Loading extensions in the background...");

		loadingThread = new Thread("extensionloader") {
			@Override
			public void run() {
				ExtensionManager.AddDiscoveryLocation(CommandHelperFileLocations.getDefault().getExtensionsDirectory());

				if (OSUtils.GetOS() == OSUtils.OS.WINDOWS) {
					// Using System.out here instead of the logger as the logger doesn't
					// immediately print to the console.
					logger.info("[CommandHelper] Caching extensions...");
					ExtensionManager.Cache(CommandHelperFileLocations.getDefault().getExtensionCacheDirectory());
					logger.info("[CommandHelper] Extension caching complete.");
				}

				ExtensionManager.Initialize(ClassDiscovery.getDefaultInstance());
				logger.info("[CommandHelper] Extension loading complete.");
			}
		};

		loadingThread.start();
	}

	public void secondSetup(String versionString) {
		if (loadingThread != null && loadingThread.isAlive()) {
			logger.info("[CommandHelper] Waiting for extension loading to complete...");

			try {
				loadingThread.join();
			} catch (InterruptedException ex) {
				logger.error(null, ex);
			}
		}

		try {
			//This may seem redundant, but on a /reload, we want to refresh these
			//properties.
			Prefs.init(CommandHelperFileLocations.getDefault().getPreferencesFile());
		} catch (IOException ex) {
			logger.error(null, ex);
		}
		if (Prefs.UseSudoFallback()) {
			logger.warn("In your preferences, use-sudo-fallback is turned on. Consider turning this off if you can.");
		}
		CHLog.initialize(CommandHelperFileLocations.getDefault().getConfigDirectory());

		version = new SimpleVersion(versionString);

		String script_name = Prefs.ScriptName();
		String main_file = Prefs.MainFile();
		boolean showSplashScreen = Prefs.ShowSplashScreen();
		if (showSplashScreen) {
			Static.getLogger().info(TermColors.reset());
			//System.out.flush();
			Static.getLogger().info("\n\n\n" + Static.Logo());
		}
		ac = new AliasCore(new File(CommandHelperFileLocations.getDefault().getConfigDirectory(), script_name),
				CommandHelperFileLocations.getDefault().getLocalPackagesDirectory(),
				CommandHelperFileLocations.getDefault().getPreferencesFile(),
				new File(CommandHelperFileLocations.getDefault().getConfigDirectory(), main_file), this);
		ac.reload(null, null);

		hostnameLookupCache = new ConcurrentHashMap<>();
		hostnameLookupThreadPool = Executors.newFixedThreadPool(3, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "CommandHelperHostnameLookup-" + (++hostnameThreadPoolID));
			}
		});
	}

	// worth noting, this is not needed if plugins can't be dynamically loaded
	public void hostCacheRefresh() {

		//Clear out our hostname cache
		hostnameLookupCache = new ConcurrentHashMap<>();
		//Create a new thread pool, with a custom ThreadFactory,
		//so we can more clearly name our threads.
		hostnameLookupThreadPool = Executors.newFixedThreadPool(3, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "CommandHelperHostnameLookup-" + (++hostnameThreadPoolID));
			}
		});
		for (MCPlayer p : Static.getServer().getOnlinePlayers()) {
			//Repopulate our cache for currently online players.
			//New players that join later will get a lookup done
			//on them at that time.
			Static.HostnameCache(p);
		}
	}

	public void disable() {
		//free up some memory
		StaticLayer.GetConvertor().runShutdownHooks();
		stopExecutionQueue();

		ExtensionManager.Cleanup();

		ac = null;
	}
}
