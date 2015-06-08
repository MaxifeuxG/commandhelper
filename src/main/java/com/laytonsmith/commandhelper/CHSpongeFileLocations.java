package com.laytonsmith.commandhelper;

import java.io.File;

/**
 * CHSpongeFileLocations, 6/5/2015 8:14 PM
 * <p/>
 * Whatever magic made getJarFile() from MethodScriptFileLocations work on Bukkit does not work on Sponge
 *
 * @author jb_aero
 */
public class CHSpongeFileLocations extends CommandHelperFileLocations {

	private final File configDir;

	public CHSpongeFileLocations(File configDirectory) {
		this.configDir = configDirectory;
	}

	@Override
	public File getConfigDirectory() {
		return configDir;
	}
}
