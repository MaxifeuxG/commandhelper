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
import com.laytonsmith.abstraction.enums.bukkit.BukkitMCEntityType;
import com.laytonsmith.core.AliasCore;
import com.laytonsmith.core.CHLog;
import com.laytonsmith.core.Installer;
import com.laytonsmith.core.Main;
import com.laytonsmith.core.MethodScriptExecutionQueue;
import com.laytonsmith.core.MethodScriptFileLocations;
import com.laytonsmith.core.Prefs;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.UpgradeLog;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.extensions.ExtensionManager;
import com.laytonsmith.core.profiler.Profiler;
import com.laytonsmith.persistence.PersistenceNetwork;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * CommandHelperMainClass, 4/6/2015
 *
 * @author jb_aero
 */
public class CommandHelperMainClass {

	public static CommandHelperMainClass self;
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

	public CommandHelperMainClass(AbstractLogger logger) {
		this.self = this;
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
		ClassDiscovery.getDefaultInstance().addDiscoveryLocation(ClassDiscovery.GetClassContainer(CommandHelperMainClass.class));
		ClassDiscovery.getDefaultInstance().addDiscoveryLocation(ClassDiscovery.GetClassContainer(apiRootClass));

		logger.info("[CommandHelper] Running initial class discovery,"
				+ " this will probably take a few seconds...");
		BukkitMCEntityType.build();

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
		if (loadingThread.isAlive()) {
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
			System.out.println(TermColors.reset());
			//System.out.flush();
			System.out.println("\n\n\n" + Static.Logo());
		}
		ac = new AliasCore(new File(CommandHelperFileLocations.getDefault().getConfigDirectory(), script_name),
				CommandHelperFileLocations.getDefault().getLocalPackagesDirectory(),
				CommandHelperFileLocations.getDefault().getPreferencesFile(),
				new File(CommandHelperFileLocations.getDefault().getConfigDirectory(), main_file), this);
		ac.reload(null, null);

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
